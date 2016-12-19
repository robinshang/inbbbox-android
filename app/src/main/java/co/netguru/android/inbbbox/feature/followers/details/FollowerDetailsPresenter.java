package co.netguru.android.inbbbox.feature.followers.details;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.details.ShotDetailsType;
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

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final FollowersController followersController;
    private final ErrorMessageController errorMessageController;
    private final CompositeSubscription subscriptions;

    @NonNull
    private Subscription loadMoreShotsSubscription;
    @NonNull
    private Subscription refreshShotsSubscription;
    @NonNull
    private Subscription unfollowUserSubscription;
    private Follower follower;
    private User user;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController, FollowersController followersController,
                             ErrorMessageController errorMessageController) {
        this.userShotsController = userShotsController;
        this.followersController = followersController;
        this.errorMessageController = errorMessageController;
        subscriptions = new CompositeSubscription();
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadMoreShotsSubscription = Subscriptions.unsubscribed();
        unfollowUserSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
            unfollowUserSubscription.unsubscribe();
        }
    }

    @Override
    public void followerDataReceived(Follower follower) {
        Timber.d("Received follower : %s", follower);
        if (follower != null) {
            this.follower = follower;
            getView().showFollowerData(follower);
            getView().showContent();
        }
    }

    @Override
    public void refreshUserShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadMoreShotsSubscription.unsubscribe();
            pageNumber = 1;
            loadMoreShotsSubscription= userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgress)
                    .subscribe(shotList -> {
                        hasMore = shotList.size() == SHOT_PAGE_COUNT;
                        getView().setData(shotList);
                    }, throwable -> Timber.e(throwable, "Error while refreshing user shots"));
        }
    }

    @Override
    public void userDataReceived(User user) {
        Timber.d("Received user : %s", user);
        if (user != null) {
            this.user = user;
            downloadUserShots(user);
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore && refreshShotsSubscription.isUnsubscribed() && loadMoreShotsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreShotsSubscription= userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .subscribe(shotList -> {
                                hasMore = shotList.size() == SHOT_PAGE_COUNT;
                                getView().showMoreUserShots(shotList);
                    }, throwable -> Timber.e(throwable, "Error while getting more user shots"));
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
                .subscribe(getView()::showFollowersList, throwable -> {
                    Timber.e(throwable, "Error while unFollow user");
                    getView().showError(throwable.getMessage());
                });
    }

    @Override
    public void showShotDetails(Shot shot) {
        if(follower != null) {
            getView().openShotDetailsScreen(shot, follower.shotList(), follower.id());
        }
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
                        throwable ->
                            handleError(throwable, "Error while getting user shots list"));
        subscriptions.add(subscription);
    }

    private void showFollower(Follower follower) {
        this.follower = follower;
        getView().showFollowerData(follower);
    }

    private void handleError(Throwable throwable, String message) {
        Timber.e(throwable, message);
        getView().showError(errorMessageController.getErrorMessageLabel(throwable));
    }
}
