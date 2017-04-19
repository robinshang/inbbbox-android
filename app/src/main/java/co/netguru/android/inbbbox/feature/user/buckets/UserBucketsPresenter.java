package co.netguru.android.inbbbox.feature.user.buckets;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

public class UserBucketsPresenter extends MvpNullObjectBasePresenter<UserBucketsContract.View>
        implements UserBucketsContract.Presenter {

    private static final int BUCKETS_PER_PAGE_COUNT = 10;
    private static final int BUCKET_SHOTS_PER_PAGE_COUNT = 30;

    private final BucketsController bucketsController;
    private final ErrorController errorController;
    private Subscription loadNextSubscription;
    private Subscription refreshSubscription;

    @NonNull
    private Subscription loadNextShotsSubscription;

    private int pageNumber = 1;
    private boolean canLoadMore = true;
    private User user;

    @Inject
    public UserBucketsPresenter(BucketsController bucketsController,
                                ErrorController errorController, User user) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextSubscription = Subscriptions.unsubscribed();
        loadNextShotsSubscription = Subscriptions.unsubscribed();
        this.user = user;
    }

    @Override
    public void loadBucketsWithShots(boolean tryFromCache) {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextSubscription.unsubscribe();
            pageNumber = 1;
            bucketsController.getUserBucketsWithShots(user.id(), pageNumber, BUCKETS_PER_PAGE_COUNT,
                    BUCKET_SHOTS_PER_PAGE_COUNT, tryFromCache)
                    .compose(applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(this::onLoadBucketsWithShotsComplete,
                            throwable -> handleError(throwable, "Error while loading buckets"));
        }
    }

    private void onLoadBucketsWithShotsComplete(List<BucketWithShots> bucketWithShotsList) {
        canLoadMore = bucketWithShotsList.size() == BUCKETS_PER_PAGE_COUNT;
        getView().setData(bucketWithShotsList);
        getView().showContent();
    }

    @Override
    public void loadMoreBucketsWithShots() {
        if (canLoadMore && refreshSubscription.isUnsubscribed()
                && loadNextSubscription.isUnsubscribed()) {
            pageNumber++;
            getView().showLoadingMoreBucketsView();
            loadNextSubscription = bucketsController.getUserBucketsWithShots
                    (user.id(), pageNumber, BUCKETS_PER_PAGE_COUNT, BUCKET_SHOTS_PER_PAGE_COUNT, false)
                    .toObservable()
                    .compose(RxTransformers.androidIO())
                    .doAfterTerminate(this::hideLoadingViews)
                    .subscribe(this::handleMoreShots,
                            throwable -> handleError(throwable, "Error while loading more buckets"));
        }
    }

    private void hideLoadingViews() {
        getView().hideProgressBars();
        getView().hideLoadingMoreBucketsView();
    }

    private void handleMoreShots(List<BucketWithShots> bucketWithShotsList) {
        canLoadMore = bucketWithShotsList.size() == BUCKETS_PER_PAGE_COUNT;
        getView().addMoreBucketsWithShots(bucketWithShotsList);
    }

    @Override
    public void handleBucketWithShotsClick(BucketWithShots bucketWithShots) {
        getView().showDetailedBucketView(bucketWithShots, BUCKET_SHOTS_PER_PAGE_COUNT);
    }

    @Override
    public void refreshBuckets() {
        loadBucketsWithShots(false);
    }

    @Override
    public void showContentForData(List<BucketWithShots> bucketsWithShotsList) {
        if (bucketsWithShotsList.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().hideEmptyView();
        }
    }

    @Override
    public void onShotClick(Shot shot, BucketWithShots bucketWithShots) {
        getView().showShotDetails(shot, bucketWithShots.shots());
    }

    @Override
    public void getMoreShotsFromBucket(BucketWithShots bucketWithShots) {
        if (bucketWithShots.hasMoreShots() && refreshSubscription.isUnsubscribed()
                && loadNextShotsSubscription.isUnsubscribed()) {
            loadNextShotsSubscription = bucketsController.getShotsFromBucket(bucketWithShots.getId(),
                    bucketWithShots.nextShotPage(), BUCKET_SHOTS_PER_PAGE_COUNT, true)
                    .compose(applySingleIoSchedulers())
                    .subscribe(shotList -> onGetBucketShotsNext(bucketWithShots.getId(), shotList),
                            throwable -> handleError(throwable, "Error while getting more project shots from server"));
        }
    }

    private void onGetBucketShotsNext(long bucketId, List<Shot> shots) {
        getView().addMoreBucketShots(bucketId, shots, BUCKET_SHOTS_PER_PAGE_COUNT);
    }

    public void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

}
