package co.netguru.android.inbbbox.feature.followers.details;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
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

    @NonNull
    private Subscription loadMoreShotsSubscription;
    @NonNull
    private Subscription refreshShotsSubscription;
    private Follower follower;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController, FollowersController followersController) {
        this.userShotsController = userShotsController;
        this.followersController = followersController;
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadMoreShotsSubscription = Subscriptions.unsubscribed();
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
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
}
