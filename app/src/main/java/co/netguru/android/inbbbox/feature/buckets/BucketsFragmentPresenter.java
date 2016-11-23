package co.netguru.android.inbbbox.feature.buckets;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class BucketsFragmentPresenter extends MvpNullObjectBasePresenter<BucketsFragmentContract.View>
        implements BucketsFragmentContract.Presenter {

    private final BucketsController bucketsController;
    private int pageNumber = 1;
    private static final int BUCKETS_PAGE_COUNT = 10;
    private static final int BUCKET_SHOT_PAGE_COUNT = 30;
    private boolean apiHasMoreBuckets = true;
    private final CompositeSubscription loadNextBucketsSubscriptions;

    @NonNull
    private Subscription refreshSubscription;

    @Inject
    BucketsFragmentPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketsSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        loadNextBucketsSubscriptions.unsubscribe();
        refreshSubscription.unsubscribe();
    }

    @Override
    public void loadBucketsWithShots(boolean isUserRefresh) {
        if (refreshSubscription.isUnsubscribed()) {
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
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketsSubscriptions.add(
                    bucketsController.getBucketWithShots(pageNumber, BUCKETS_PAGE_COUNT, BUCKET_SHOT_PAGE_COUNT)
                            .doAfterTerminate(getView()::hideProgressBars)
                            .compose(RxTransformerUtils.applySingleIoSchedulers())
                            .subscribe(bucketWithShotsList -> {
                                apiHasMoreBuckets = bucketWithShotsList.size() == BUCKETS_PAGE_COUNT;
                                getView().addMoreBucketsWithShots(bucketWithShotsList);
                            }, throwable -> Timber.d(throwable, "Error while loading more buckets"))
            );
        }
    }
}