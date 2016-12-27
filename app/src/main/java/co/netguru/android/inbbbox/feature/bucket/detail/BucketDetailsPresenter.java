package co.netguru.android.inbbbox.feature.bucket.detail;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

@FragmentScope
public class BucketDetailsPresenter extends MvpNullObjectBasePresenter<BucketDetailsContract.View>
        implements BucketDetailsContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final BucketsController bucketsController;
    private final ErrorController errorController;

    private int shotsPerPage;
    private int pageNumber = 1;
    private boolean canLoadMore;

    private long currentBucketId;
    private String currentBucketName;

    @NonNull
    protected Subscription refreshShotsSubscription;
    @NonNull
    protected Subscription loadNextShotsSubscription;

    @Inject
    public BucketDetailsPresenter(BucketsController bucketsController, ErrorController errorController) {
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
        List<ShotEntity> shotEntities = bucketWithShots.shots();
        getView().setFragmentTitle(bucket.name());
        handleNewShots(shotEntities);
    }

    @Override
    public void refreshShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadNextShotsSubscription.unsubscribe();
            pageNumber = 1;
            refreshShotsSubscription = bucketsController.getShotsListFromBucket(currentBucketId, pageNumber, shotsPerPage)
                    .compose(RxTransformerUtil.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressbar)
                    .subscribe(this::handleNewShots,
                            throwable -> handleError(throwable, "Error while refreshing shots"));
        }
    }

    @Override
    public void checkDataEmpty(List<ShotEntity> data) {
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
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
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
                            .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                                    SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS, getView()::showLoadingMoreShotsView))
                            .compose(RxTransformers.androidIO())
                            .doAfterTerminate(getView()::hideLoadingMoreShotsView)
                            .subscribe(shotEntities -> {
                                getView().addShots(shotEntities);
                                canLoadMore = shotEntities.size() == shotsPerPage;
                            }, throwable -> handleError(throwable, "Error while loading new shots from bucket"));
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void handleNewShots(List<ShotEntity> shotEntities) {
        canLoadMore = shotEntities.size() == shotsPerPage;
        getView().setData(shotEntities);
        getView().showContent();
    }
}
