package co.netguru.android.inbbbox.feature.bucket;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.events.ShotRemovedFromBucketEvent;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class BucketsFragmentPresenter extends MvpNullObjectBasePresenter<BucketsFragmentContract.View>
        implements BucketsFragmentContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;
    private static final int BUCKETS_PER_PAGE_COUNT = 10;
    private static final int BUCKET_SHOTS_PER_PAGE_COUNT = 30;

    private final BucketsController bucketsController;
    private final ErrorController errorController;
    private final RxBus rxBus;

    private int pageNumber = 1;
    private boolean apiHasMoreBuckets = true;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;
    private Subscription bucketCreatedBusSubscription;
    private Subscription shotRemovedBusSubscription;

    @Inject
    BucketsFragmentPresenter(BucketsController bucketsController, RxBus rxBus, ErrorController errorController) {
        this.bucketsController = bucketsController;
        this.rxBus = rxBus;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        bucketCreatedBusSubscription.unsubscribe();
        shotRemovedBusSubscription.unsubscribe();
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextBucketSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(BucketsFragmentContract.View view) {
        super.attachView(view);
        setupBucketCreatedRxBus();
        setupShotRemovedRxBus();
    }

    @Override
    public void loadBucketsWithShots() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = bucketsController.getUserBucketsWithShots(pageNumber, BUCKETS_PER_PAGE_COUNT, BUCKET_SHOTS_PER_PAGE_COUNT)
                    .compose(RxTransformerUtil.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(bucketWithShotsList -> {
                                apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PER_PAGE_COUNT;
                                getView().setData(bucketWithShotsList);
                                getView().showContent();
                            },
                            throwable -> handleError(throwable, "Error while loading buckets"));
        }
    }

    @Override
    public void loadMoreBucketsWithShots() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription = bucketsController.getUserBucketsWithShots(pageNumber, BUCKETS_PER_PAGE_COUNT, BUCKET_SHOTS_PER_PAGE_COUNT)
                    .toObservable()
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreBucketsView))
                    .compose(RxTransformers.androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBars();
                        getView().hideLoadingMoreBucketsView();
                    })
                    .subscribe(bucketWithShotsList -> {
                        apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PER_PAGE_COUNT;
                        getView().addMoreBucketsWithShots(bucketWithShotsList);
                    }, throwable -> handleError(throwable, "Error while loading more buckets"));
        }
    }

    @Override
    public void handleBucketWithShotsClick(BucketWithShots bucketWithShots) {
        getView().showDetailedBucketView(bucketWithShots, BUCKET_SHOTS_PER_PAGE_COUNT);
    }

    @Override
    public void handleCreateBucket() {
        getView().openCreateDialogFragment();
    }

    @Override
    public void checkEmptyData(List<BucketWithShots> data) {
        if (data.isEmpty()) {
            getView().showEmptyBucketView();
        } else {
            getView().hideEmptyBucketView();
        }
    }

    @Override
    public void handleDeleteBucket(long deletedBucketId) {
        getView().removeBucketFromList(deletedBucketId);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void setupBucketCreatedRxBus() {
        bucketCreatedBusSubscription = rxBus.getEvents(BucketCreatedEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(bucketCreatedEvent -> {
                    BucketWithShots bucketWithShots = BucketWithShots.create(bucketCreatedEvent.getBucket(),
                            Collections.emptyList());
                    getView().addNewBucketWithShotsOnTop(bucketWithShots);
                    getView().scrollToTop();
                });
    }

    private void setupShotRemovedRxBus() {
        shotRemovedBusSubscription = rxBus.getEvents(ShotRemovedFromBucketEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(bucketCreatedEvent -> loadBucketsWithShots());
    }
}