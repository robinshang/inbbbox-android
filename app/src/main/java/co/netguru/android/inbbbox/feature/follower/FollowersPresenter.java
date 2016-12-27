package co.netguru.android.inbbbox.feature.follower;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersShotController;
import co.netguru.android.inbbbox.data.follower.model.ui.Follower;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private static final int FOLLOWERS_PAGE_COUNT = 15;
    private static final int FOLLOWERS_SHOT_PAGE_COUNT = 30;
    private static final int FOLLOWERS_SHOT_PAGE_NUMBER = 1;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final FollowersController followersController;
    private final FollowersShotController followersShotController;
    private final ErrorController errorController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowersPresenter(FollowersController followersController, FollowersShotController followersShotController,
                       ErrorController errorController) {
        this.followersController = followersController;
        this.followersShotController = followersShotController;
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
    public void getFollowedUsersFromServer() {
        if (refreshSubscription.isUnsubscribed()) {
            pageNumber = 1;
            loadNextBucketSubscription.unsubscribe();
            refreshSubscription = followersController.getFollowedUsers(pageNumber, FOLLOWERS_PAGE_COUNT)
                    .flatMap(follower -> followersShotController.getFollowedUserWithShots(follower,
                            FOLLOWERS_SHOT_PAGE_NUMBER, FOLLOWERS_SHOT_PAGE_COUNT))
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
            loadNextBucketSubscription = followersController.getFollowedUsers(pageNumber, FOLLOWERS_PAGE_COUNT)
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreFollowersView))
                    .flatMap(follower -> followersShotController.getFollowedUserWithShots(follower,
                            FOLLOWERS_SHOT_PAGE_NUMBER, FOLLOWERS_SHOT_PAGE_COUNT))
                    .toList()
                    .compose(androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBars();
                        getView().hideLoadingMoreBucketsView();
                    })
                    .subscribe(this::onGetMoreFollowersNext,
                            throwable -> handleError(throwable, "Error while getting followed users form server"));
        }
    }

    @Override
    public void checkDataEmpty(List<Follower> data) {
        if (data.isEmpty()) {
            getView().showEmptyLikesInfo();
        } else {
            getView().hideEmptyLikesInfo();
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onGetFollowersNext(List<Follower> followersList) {
        hasMore = followersList.size() == FOLLOWERS_PAGE_COUNT;
        getView().setData(followersList);
        getView().showContent();
    }

    private void onGetMoreFollowersNext(List<Follower> followersList) {
        hasMore = followersList.size() == FOLLOWERS_PAGE_COUNT;
        getView().showMoreFollowedUsers(followersList);
    }
}
