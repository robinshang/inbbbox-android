package co.netguru.android.inbbbox.feature.buckets;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.event.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
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
    private Subscription busSubscription;

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
        refreshSubscription.unsubscribe();
        loadNextBucketSubscription.unsubscribe();
        busSubscription.unsubscribe();
    }

    @Override
    public void attachView(BucketsFragmentContract.View view) {
        super.attachView(view);
        setupRxBus();
    }

    @Override
    public void loadBucketsWithShots(boolean isUserRefresh) {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = bucketsController.getUserBucketsWithShots(pageNumber, BUCKETS_PER_PAGE_COUNT, BUCKET_SHOTS_PER_PAGE_COUNT)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(bucketWithShotsList -> {
                                apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PER_PAGE_COUNT;
                                if (bucketWithShotsList.isEmpty()) {
                                    getView().showEmptyBucketView();
                                } else {
                                    getView().showBucketsWithShots(bucketWithShotsList);
                                }
                            },
                            throwable -> handleHttpErrorResponse(throwable, "Error while loading buckets"));
        }
    }

    @Override
    public void loadMoreBucketsWithShots() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription = bucketsController.getUserBucketsWithShots(pageNumber, BUCKETS_PER_PAGE_COUNT, BUCKET_SHOTS_PER_PAGE_COUNT)
                    .toObservable()
                    .compose(RxTransformerUtils.executeRunnableIfObservableDidntEmitUntilGivenTime(
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
                    }, throwable -> handleHttpErrorResponse(throwable, "Error while loading more buckets"));
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
    public void handleHttpErrorResponse(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void setupRxBus() {
        busSubscription = rxBus.getEvents(BucketCreatedEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(bucketCreatedEvent -> {
                    BucketWithShots bucketWithShots = BucketWithShots.create(bucketCreatedEvent.getBucket(),
                            Collections.emptyList());
                    getView().addNewBucketWithShotsOnTop(bucketWithShots);
                    getView().scrollToTop();
                });
    }
}