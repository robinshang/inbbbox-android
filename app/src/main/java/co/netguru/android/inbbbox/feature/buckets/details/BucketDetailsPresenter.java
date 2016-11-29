package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

@FragmentScope
public class BucketDetailsPresenter extends MvpNullObjectBasePresenter<BucketDetailsContract.View>
        implements BucketDetailsContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final BucketsController bucketsController;

    private int shotsPerPage;
    private long currentBucketId;
    private int pageNumber = 1;
    @NonNull
    private Subscription loadNextBucketSubscription;

    @Inject
    public BucketDetailsPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void handleBucketData(BucketWithShots bucketWithShots, int perPage) {
        Bucket bucket = bucketWithShots.bucket();
        currentBucketId = bucket.id();
        List<ShotEntity> shotEntities = bucketWithShots.shots();
        shotsPerPage = perPage;
        getView().setFragmentTitle(bucket);
        getView().setShots(shotEntities);
    }

    @Override
    public void loadMoreShots() {
        if (loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription =
                    bucketsController.getShotsListFromBucket(currentBucketId,pageNumber,shotsPerPage)
                    .toObservable()
                    .publish(listObservable -> listObservable.timeout(SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            Observable.<List<BucketWithShots>>fromCallable(() -> {
                                getView().showLoadingMoreBucketsView();
                                return null;
                            })
                                    .ignoreElements()
                                    .mergeWith(listObservable))
                    )
        }
    }


}
