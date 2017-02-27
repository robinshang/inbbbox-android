package co.netguru.android.inbbbox.feature.user;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.SerialSubscription;
import timber.log.Timber;

public class UserActivityPresenter extends MvpNullObjectBasePresenter<UserActivityContract.View>
        implements UserActivityContract.Presenter {

    private final FollowersController followersController;
    private final ErrorController errorController;
    private final SerialSubscription isFollowingSubscription = new SerialSubscription();
    private final CompositeSubscription followSubscription = new CompositeSubscription();
    private final CompositeSubscription unfollowSubscription = new CompositeSubscription();

    @Inject
    public UserActivityPresenter(FollowersController followersController,
                                 ErrorController errorController) {
        this.followersController = followersController;
        this.errorController = errorController;
    }

    @Override
    public void checkFollowingStatus(User user) {
        isFollowingSubscription.set(followersController
                .isUserFollowed(user.id())
                .compose(RxTransformerUtil.applySingleIoSchedulers())
                .subscribe(following -> getView().showFollowingAction(!following),
                        e -> handleError(e, "Could not check following status")));
    }

    @Override
    public void startFollowing(User user) {
        getView().showFollowingAction(false);
        followSubscription.add(followersController
                .followUser(user)
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> getView().showFollowingAction(false),
                        e -> {
                            getView().showFollowingAction(true);
                            handleError(e, "Could not start following user");
                        }));
    }

    @Override
    public void stopFollowing(User user) {
        getView().showFollowingAction(true);
        unfollowSubscription.add(followersController
                .unFollowUser(user.id())
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> getView().showFollowingAction(true),
                        e -> {
                            getView().showFollowingAction(false);
                            handleError(e, "Could not start following user");
                        }));
    }

    private void handleError(Throwable e, String errorText) {
        Timber.e(e, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(e));
    }

    @Override
    public void detachView(boolean retainInstance) {
        followSubscription.clear();
        unfollowSubscription.clear();
        isFollowingSubscription.unsubscribe();
        super.detachView(retainInstance);
    }
}
