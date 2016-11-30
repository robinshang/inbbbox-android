package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
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
    private final CompositeSubscription subscriptions;

    private String name;
    private long id;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController, FollowersController followersController) {
        this.userShotsController = userShotsController;
        this.followersController = followersController;
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
            this.name = follower.name();
            this.id = follower.id();
            getView().showFollowerData(follower);
        }
    }

    @Override
    public void userDataReceived(User user) {
        Timber.d("Received user : %s", user);
        if (user != null) {
            this.name = user.name();
            this.id = user.id();
            getUserShots(user);
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore) {
            pageNumber++;
            final Subscription subscription = userShotsController.getUserShotsList(id,
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .subscribe(this::onGetUserShotsNext,
                            throwable -> Timber.e(throwable, "Error while getting more user shots"));
            subscriptions.add(subscription);
        }
    }

    @Override
    public void onUnFollowClick() {
        getView().showUnFollowDialog(name);
    }

    @Override
    public void unFollowUser() {
        final Subscription subscription = followersController.unFollowUser(id)
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

    private void getUserShots(User user) {
        final Subscription subscription = userShotsController.getUserShotsList(id,
                pageNumber, SHOT_PAGE_COUNT)
                .compose(androidIO())
                .subscribe(list -> getView().showUserData(user, list),
                        throwable -> Timber.e(throwable, "Error while getting more user shots"));
        subscriptions.add(subscription);
    }
}
