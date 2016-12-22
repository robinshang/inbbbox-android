package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

@FragmentScope
public class BucketDetailsPresenter extends MvpNullObjectBasePresenter<BucketDetailsContract.View>
        implements BucketDetailsContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    @VisibleForTesting
    @NonNull
    Subscription refreshShotsSubscription;
    @VisibleForTesting
    @NonNull
    Subscription loadNextShotsSubscription;

    private final BucketsController bucketsController;
    private final ErrorController errorController;

    private int shotsPerPage;
    private int pageNumber = 1;
    private boolean canLoadMore;

    private long currentBucketId;
    private String currentBucketName;

    @Inject
    BucketDetailsPresenter(BucketsController bucketsController, ErrorController errorController) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadNextShotsSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshShotsSubscription.unsubscribe();
        loadNextShotsSubscription.unsubscribe();
    }

    @Override
    public void handleBucketData(BucketWithShots bucketWithShots, int perPage) {
        shotsPerPage = perPage;
        Bucket bucket = bucketWithShots.bucket();
        currentBucketId = bucket.id();
        currentBucketName = bucket.name();
        List<Shot> shots = bucketWithShots.shots();
        getView().setFragmentTitle(bucket.name());
        handleNewShots(shots);
    }

    @Override
    public void refreshShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadNextShotsSubscription.unsubscribe();
            pageNumber = 1;
            refreshShotsSubscription = bucketsController.getShotsListFromBucket(currentBucketId, pageNumber, shotsPerPage)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressbar)
                    .subscribe(this::handleNewShots,
                            throwable -> handleError(throwable, "Error while refreshing shots"));
        }
    }

    @Override
    public void checkDataEmpty(List<Shot> data) {
        if (data.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().hideEmptyView();
        }
    }

    @Override
    public void onDeleteBucketClick() {
        getView().showRemoveBucketDialog(currentBucketName);
    }

    @Override
    public void deleteBucket() {
        bucketsController.deleteBucket(currentBucketId)
                .compose(RxTransformerUtils.applyCompletableIoSchedulers())
                .subscribe(() -> getView().showRefreshedBucketsView(currentBucketId),
                        throwable -> handleError(throwable, "Error while deleting bucket"));
    }

    @Override
    public void loadMoreShots() {
        if (canLoadMore && refreshShotsSubscription.isUnsubscribed() && loadNextShotsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextShotsSubscription =
                    bucketsController.getShotsListFromBucket(currentBucketId, pageNumber, shotsPerPage)
                            .toObservable()
                            .compose(RxTransformerUtils.executeRunnableIfObservableDidntEmitUntilGivenTime(
                                    SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS, getView()::showLoadingMoreShotsView))
                            .compose(RxTransformers.androidIO())
                            .doAfterTerminate(getView()::hideLoadingMoreShotsView)
                            .subscribe(shots -> {
                                getView().addShots(shots);
                                canLoadMore = shots.size() == shotsPerPage;
                            }, throwable -> handleError(throwable, "Error while loading new shots from bucket"));
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void handleNewShots(List<Shot> shots) {
        canLoadMore = shots.size() == shotsPerPage;
        getView().setData(shots);
        getView().showContent();
    }
}
