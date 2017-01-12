package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class RemoveFromBucketPresenter extends MvpNullObjectBasePresenter<RemoveFromBucketContract.View>
        implements RemoveFromBucketContract.Presenter {

    private static final int BUCKETS_PER_PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final BucketsController bucketsController;
    private final ErrorController errorController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketsSubscription;

    private Shot shot;
    private int pageNumber = 1;
    private boolean apiHasMoreBuckets = true;

    @Inject
    RemoveFromBucketPresenter(BucketsController bucketsController, ErrorController errorController) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketsSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshSubscription.unsubscribe();
        loadNextBucketsSubscription.unsubscribe();
    }

    @Override
    public void handleShot(Shot shot) {
        this.shot = shot;
        getView().setShotTitle(shot.title());
        getView().showShotPreview(shot.normalImageUrl());
        getView().showAuthorAvatar(shot.author().avatarUrl());
        if (shot.team() != null) {
            getView().showShotAuthorAndTeam(shot.author().name(), shot.team().name());
        } else {
            getView().showShotAuthor(shot.author().name());
        }
        getView().showShotCreationDate(shot.creationDate());
    }

    @Override
    public void loadBucketsForShot() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketsSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = bucketsController.getListBucketsForShot(shot.id())
                    .doOnSubscribe(getView()::showBucketListLoading)
                    .compose(RxTransformerUtil.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(buckets -> {
                        apiHasMoreBuckets = buckets.size() == BUCKETS_PER_PAGE_COUNT;
                        getView().setBucketsList(buckets);
                        getView().showBucketsList();
                    }, throwable -> handleError(throwable, "Error occurred while requesting buckets"));
        }
    }

    @Override
    public void loadMoreBuckets() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed() && loadNextBucketsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketsSubscription = bucketsController.getListBucketsForShot(shot.id())
                    .toObservable()
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showBucketListLoadingMore))
                    .compose(RxTransformers.androidIO())
                    .subscribe(buckets -> {
                        apiHasMoreBuckets = buckets.size() == BUCKETS_PER_PAGE_COUNT;
                        getView().showMoreBuckets(buckets);
                    }, throwable -> handleError(throwable, "Error occurred while requesting more buckets"));
        }
    }

    @Override
    public void handleBucketClick(Bucket bucket) {
        getView().passResultAndCloseFragment(bucket, shot);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void onOpenShotFullscreen() {
        getView().openShotFullscreen(shot);
    }

}
