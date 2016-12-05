package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applyCompletableIoSchedulers;

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final FollowersController followersController;
    private final ErrorMessageController errorMessageController;
    private final CompositeSubscription subscriptions;

    private Follower follower;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController, FollowersController followersController,
                             ErrorMessageController errorMessageController) {
        this.userShotsController = userShotsController;
        this.followersController = followersController;
        this.errorMessageController = errorMessageController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void followerDataReceived(Follower follower) {
        Timber.d("Received follower : %s", follower);
        if (follower != null) {
            this.follower = follower;
            getView().showFollowerData(follower);
        }
    }

    @Override
    public void userDataReceived(User user) {
        Timber.d("Received user : %s", user);
        if (user != null) {
            downloadUserShots(user);
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore) {
            pageNumber++;
            final Subscription subscription = userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .subscribe(this::onGetUserShotsNext,
                            throwable -> Timber.e(throwable, "Error while getting more user shots"));
            subscriptions.add(subscription);
        }
    }

    @Override
    public void onUnFollowClick() {
        getView().showUnFollowDialog(follower.name());
    }

    @Override
    public void unFollowUser() {
        final Subscription subscription = followersController.unFollowUser(follower.id())
                .compose(applyCompletableIoSchedulers())
                .subscribe(getView()::showFollowersList, throwable -> {
                    Timber.e(throwable, "Error while unFollow user");
                    getView().showError(throwable.getMessage());
                });
        subscriptions.add(subscription);
    }

    @Override
    public void showShotDetails(Shot shot) {
        getView().openShotDetailsScreen(shot);
    }

    private void onGetUserShotsNext(List<Shot> shotList) {
        hasMore = shotList.size() == SHOT_PAGE_COUNT;
        getView().showMoreUserShots(shotList);
    }

    private void downloadUserShots(User user) {
        final Subscription subscription = userShotsController.getUserShotsList(user.id(),
                pageNumber, SHOT_PAGE_COUNT)
                .compose(androidIO())
                .subscribe(list -> setUserOnShot(user, list),
                        throwable -> {
                            handleError(throwable, "Error while getting user shots list");
                        });
        subscriptions.add(subscription);
    }

    private void setUserOnShot(User user, List<Shot> list) {
        List<Shot> shotsList = new ArrayList<>();
        for (Shot listShot: list) {
            shotsList.add(Shot.update(listShot).author(user).build());
        }

        createFollower(user, shotsList);
    }

    private void createFollower(User user, List<Shot> list) {
        this.follower = Follower.createFromUser(user, list);
        getView().showFollowerData(follower);
    }

    private void handleError(Throwable throwable, String message) {
        Timber.e(throwable, message);
        getView().showError(errorMessageController.getErrorMessageLabel(throwable));
    }
}
