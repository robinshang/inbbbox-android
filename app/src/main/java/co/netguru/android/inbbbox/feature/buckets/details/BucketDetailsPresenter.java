package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

@FragmentScope
public class BucketDetailsPresenter extends MvpNullObjectBasePresenter<BucketDetailsContract.View>
        implements BucketDetailsContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final BucketsController bucketsController;

    private int shotsPerPage;
    private int pageNumber = 1;
    private boolean canLoadMore;

    private long currentBucketId;
    
    @NonNull
    private Subscription refreshShotsSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;

    @Inject
    public BucketDetailsPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshShotsSubscription.unsubscribe();
        loadNextBucketSubscription.unsubscribe();
    }

    @Override
    public void handleBucketData(BucketWithShots bucketWithShots, int perPage) {
        shotsPerPage = perPage;
        Bucket bucket = bucketWithShots.bucket();
        currentBucketId = bucket.id();
        List<ShotEntity> shotEntities = bucketWithShots.shots();
        getView().setFragmentTitle(bucket);
        handleNewShots(shotEntities);
    }

    @Override
    public void refreshShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadNextBucketSubscription.unsubscribe();
            pageNumber = 1;
            refreshShotsSubscription = bucketsController.getShotsListFromBucket(currentBucketId, pageNumber, shotsPerPage)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressbar)
                    .subscribe(this::handleNewShots,
                            throwable -> Timber.d(throwable, "Error while loading new shots from bucket")
                    );
        }
    }

    @Override
    public void loadMoreShots() {
        if (canLoadMore && refreshShotsSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription =
                    bucketsController.getShotsListFromBucket(currentBucketId, pageNumber, shotsPerPage)
                            .toObservable()
                            .compose(RxTransformerUtils.executeRunnableIfObservableDidntEmitUntilGivenTime(
                                    SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS, getView()::showLoadingMoreShotsView))
                            .compose(RxTransformers.androidIO())
                            .doAfterTerminate(getView()::hideLoadingMoreShotsView)
                            .subscribe(shotEntities -> {
                                getView().addShots(shotEntities);
                                canLoadMore = shotEntities.size() == shotsPerPage;
                            }, throwable -> Timber.d(throwable, "Error while loading new shots from bucket"));
        }
    }

    private void handleNewShots(List<ShotEntity> shotEntities) {
        canLoadMore = shotEntities.size() == shotsPerPage;
        if (shotEntities.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().showShots(shotEntities);
        }
    }
}
