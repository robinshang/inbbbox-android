package co.netguru.android.inbbbox.feature.followers.details;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.controler.followers.FollowersController;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applySingleIoSchedulers;

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final FollowersController followersController;
    private final ErrorController errorController;
    private final CompositeSubscription subscriptions;

    @NonNull
    private Subscription loadMoreShotsSubscription;
    @NonNull
    private Subscription refreshShotsSubscription;
    @NonNull
    private Subscription unfollowUserSubscription;
    @NonNull
    private Subscription followUserSubscription;
    private Follower follower;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController, FollowersController followersController,
                             ErrorController errorController) {
        this.userShotsController = userShotsController;
        this.followersController = followersController;
        this.errorController = errorController;
        subscriptions = new CompositeSubscription();
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadMoreShotsSubscription = Subscriptions.unsubscribed();
        unfollowUserSubscription = Subscriptions.unsubscribed();
        followUserSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
            unfollowUserSubscription.unsubscribe();
            followUserSubscription.unsubscribe();
        }
    }

    @Override
    public void followerDataReceived(Follower follower) {
        Timber.d("Received follower : %s", follower);
        if (follower != null) {
            this.follower = follower;
            checkIfUserIsFollowed(follower.id());
            showFollower(follower);
        }
    }

    @Override
    public void refreshUserShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadMoreShotsSubscription.unsubscribe();
            pageNumber = 1;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgress)
                    .subscribe(shotList -> {
                        hasMore = shotList.size() == SHOT_PAGE_COUNT;
                        getView().setData(shotList);
                    }, throwable -> handleError(throwable, "Error while refreshing user shots"));
        }
    }

    @Override
    public void userDataReceived(User user) {
        Timber.d("Received user : %s", user);
        if (user != null) {
            downloadUserShots(user);
            checkIfUserIsFollowed(user.id());
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore && refreshShotsSubscription.isUnsubscribed() && loadMoreShotsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(User.createFromFollower(follower)).build())
                    .toList()
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        hasMore = shotList.size() == SHOT_PAGE_COUNT;
                        getView().showMoreUserShots(shotList);
                    }, throwable -> handleError(throwable, "Error while getting more user shots"));
        }
    }

    @Override
    public void onUnFollowClick() {
        getView().showUnFollowDialog(follower.name());
    }

    @Override
    public void unFollowUser() {
        unfollowUserSubscription = followersController.unFollowUser(follower.id())
                .compose(applyCompletableIoSchedulers())
                .subscribe(getView()::showFollowersList,
                        throwable -> handleError(throwable, "Error while unFollow user"));
    }

    @Override
    public void showShotDetails(Shot shot) {
        if (follower != null) {
            getView().openShotDetailsScreen(shot, follower.shotList(), follower.id());
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void onFollowClick() {
        getView().showFollowDialog(follower.name());
    }

    @Override
    public void followUser() {
        followUserSubscription = followersController.followUser(follower.id())
                .compose(applyCompletableIoSchedulers())
                .subscribe(() -> checkIfUserIsFollowed(follower.id()),
                        throwable -> handleError(throwable, "Error while follow user"));
    }

    private void downloadUserShots(User user) {
        final Subscription subscription = userShotsController.getUserShotsList(user.id(),
                pageNumber, SHOT_PAGE_COUNT)
                .compose(androidIO())
                .compose(fromListObservable())
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(list -> Follower.createFromUser(user, list))
                .subscribe(this::showFollower,
                        throwable -> handleError(throwable, "Error while getting user shots list"));
        subscriptions.add(subscription);
    }

    private void showFollower(Follower follower) {
        this.follower = follower;
        getView().showFollowerData(follower);
        getView().showContent();
    }

    private void checkIfUserIsFollowed(long userId) {
        followersController.isUserFollowed(userId)
                .compose(applySingleIoSchedulers())
                .subscribe(this::setFollowingMenuIcon,
                        throwable -> handleError(throwable, "Error while checking if user is followed"));
    }

    private void setFollowingMenuIcon(boolean isFollowed) {
        getView().setFollowingMenuIcon(isFollowed);
    }

}
