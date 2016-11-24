package co.netguru.android.inbbbox.feature.buckets;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class BucketsFragmentPresenter extends MvpNullObjectBasePresenter<BucketsFragmentContract.View>
        implements BucketsFragmentContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;
    private final BucketsController bucketsController;
    private int pageNumber = 1;
    private static final int BUCKETS_PAGE_COUNT = 10;
    private static final int BUCKET_SHOT_PAGE_COUNT = 30;
    private boolean apiHasMoreBuckets = true;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;

    @Inject
    BucketsFragmentPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshSubscription.unsubscribe();
        loadNextBucketSubscription.unsubscribe();
    }

    @Override
    public void loadBucketsWithShots(boolean isUserRefresh) {
        if (refreshSubscription.isUnsubscribed()) {
            pageNumber = 1;
            refreshSubscription = bucketsController.getBucketWithShots(pageNumber, BUCKETS_PAGE_COUNT, BUCKET_SHOT_PAGE_COUNT)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(bucketWithShotsList -> {
                                if (bucketWithShotsList.isEmpty()) {
                                    getView().showEmptyBucketView();
                                } else {
                                    apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PAGE_COUNT;
                                    getView().showBucketsWithShots(bucketWithShotsList);
                                }
                            },
                            throwable -> Timber.d(throwable, "Error while loading buckets"));
        }
    }

    @Override
    public void loadMoreBucketsWithShots() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription = bucketsController.getBucketWithShots(pageNumber, BUCKETS_PAGE_COUNT, BUCKET_SHOT_PAGE_COUNT)
                    .toObservable()
                    .publish(listObservable -> listObservable.timeout(SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            Observable.<List<BucketWithShots>>fromCallable(() -> {
                                getView().showLoadingMoreBucketsView();
                                return null;
                            })
                                    .ignoreElements()
                                    .mergeWith(listObservable))
                    )
                    .compose(RxTransformers.androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBars();
                        getView().hideLoadingMoreBucketsView();
                    })
                    .subscribe(bucketWithShotsList -> {
                        apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PAGE_COUNT;
                        getView().addMoreBucketsWithShots(bucketWithShotsList);
                    }, throwable -> Timber.d(throwable, "Error while loading more buckets"));
        }
    }
}