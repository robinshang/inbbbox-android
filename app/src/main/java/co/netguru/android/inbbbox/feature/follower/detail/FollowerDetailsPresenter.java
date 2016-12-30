package co.netguru.android.inbbbox.feature.follower.detail;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

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

    private User follower;
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
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
        }
    }

    @Override
    public void userDataReceived(User user) {
        Timber.d("Received user : %s", user);
        if (user != null) {
            checkIfUserIsFollowed(user.id());
            showUserData(user);
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
    public void getMoreUserShotsFromServer() {
        if (hasMore && refreshShotsSubscription.isUnsubscribed() && loadMoreShotsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(follower).build())
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
        subscriptions.add(
                followersController.unFollowUser(follower.id())
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(getView()::showFollowersList,
                                throwable -> handleError(throwable, "Error while unFollow user")));
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
        subscriptions.add(
                followersController.followUser(follower)
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(() -> setFollowingMenuIcon(true),
                                throwable -> handleError(throwable, "Error while follow user")));
    }

    private void showUserData(User user) {
        if (user.shotList() == null) {
            downloadUserShots(user);
        } else {
            showFollower(user);
        }
    }

    private void downloadUserShots(User user) {
        final Subscription subscription = userShotsController.getUserShotsList(user.id(),
                pageNumber, SHOT_PAGE_COUNT)
                .compose(androidIO())
                .compose(fromListObservable())
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(list -> User.updateUserShots(user, list))
                .subscribe(this::showFollower,
                        throwable -> handleError(throwable, "Error while getting user shots list"));
        subscriptions.add(subscription);
    }

    private void showFollower(User follower) {
        this.follower = follower;
        getView().showFollowerData(follower);
        getView().showContent();
    }

    private void checkIfUserIsFollowed(long userId) {
        subscriptions.add(
                followersController.isUserFollowed(userId)
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::setFollowingMenuIcon,
                                throwable -> handleError(throwable, "Error while checking if user is followed")));
    }

    private void setFollowingMenuIcon(boolean isFollowed) {
        getView().setFollowingMenuIcon(isFollowed);
    }
}
