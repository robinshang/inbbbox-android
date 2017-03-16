package co.netguru.android.inbbbox.feature.follower;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private static final int FOLLOWERS_PAGE_COUNT = 10;
    private static final int FOLLOWERS_SHOT_PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final FollowersController followersController;
    private final ErrorController errorController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowersPresenter(FollowersController followersController, ErrorController errorController) {
        this.followersController = followersController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextBucketSubscription.unsubscribe();
        }
    }

    @Override
    public void getFollowedUsersFromServer(boolean canUseCacheForShots) {
        if (refreshSubscription.isUnsubscribed()) {
            pageNumber = 1;
            loadNextBucketSubscription.unsubscribe();
            refreshSubscription = followersController.getFollowedUsers(pageNumber,
                    FOLLOWERS_PAGE_COUNT, FOLLOWERS_SHOT_PAGE_COUNT, canUseCacheForShots)
                    .toList()
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(this::onGetFollowersNext,
                            throwable -> handleError(throwable, "Error while getting followed users form server"));
        }
    }

    @Override
    public void getMoreFollowedUsersFromServer() {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription = followersController.getFollowedUsers(pageNumber,
                    FOLLOWERS_PAGE_COUNT, FOLLOWERS_SHOT_PAGE_COUNT, false)
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreFollowersView))
                    .toList()
                    .compose(androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBars();
                        getView().hideLoadingMoreFollowersView();
                    })
                    .subscribe(this::onGetMoreFollowersNext,
                            throwable -> handleError(throwable, "Error while getting followed users form server"));
        }
    }

    @Override
    public void checkDataEmpty(List<UserWithShots> data) {
        if (data.isEmpty()) {
            getView().showEmptyFollowersInfo();
        } else {
            getView().hideEmptyFollowersInfo();
        }
    }

    @Override
    public void onFollowedUserSelect(UserWithShots userWithShots) {
        getView().openUserDetails(userWithShots);
    }

    @Override
    public void refreshFollowedUsers() {
        getFollowedUsersFromServer(false);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onGetFollowersNext(List<UserWithShots> userWithShotsList) {
        hasMore = userWithShotsList.size() >= FOLLOWERS_PAGE_COUNT;
        getView().setData(userWithShotsList);
        getView().showContent();
    }

    private void onGetMoreFollowersNext(List<UserWithShots> userWithShotsList) {
        hasMore = userWithShotsList.size() >= FOLLOWERS_PAGE_COUNT;
        getView().showMoreFollowedUsers(userWithShotsList);
    }
}
