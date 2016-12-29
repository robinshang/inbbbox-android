package co.netguru.android.inbbbox.feature.shots.addtobucket;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.event.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class AddToBucketPresenter extends MvpNullObjectBasePresenter<AddToBucketContract.View>
        implements AddToBucketContract.Presenter {

    private static final int BUCKETS_PER_PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final BucketsController bucketsController;
    private final ErrorController errorController;

    private final RxBus rxBus;
    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketsSubscription;
    private Subscription busSubscription;

    private Shot shot;
    private int pageNumber = 1;
    private boolean apiHasMoreBuckets = true;

    @Inject
    AddToBucketPresenter(BucketsController bucketsController, ErrorController errorController, RxBus rxBus) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        this.rxBus = rxBus;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketsSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        busSubscription.unsubscribe();
        refreshSubscription.unsubscribe();
        loadNextBucketsSubscription.unsubscribe();
    }

    @Override
    public void attachView(AddToBucketContract.View view) {
        super.attachView(view);
        setupRxBus();
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
    public void loadAvailableBuckets() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketsSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = bucketsController.getCurrentUserBuckets(pageNumber, BUCKETS_PER_PAGE_COUNT)
                    .doOnSubscribe(getView()::showBucketListLoading)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(buckets -> {
                        apiHasMoreBuckets = buckets.size() == BUCKETS_PER_PAGE_COUNT;
                        if (buckets.isEmpty()) {
                            getView().showNoBucketsAvailable();
                        } else {
                            getView().setBucketsList(buckets);
                            getView().showBucketsList();
                        }
                    }, throwable -> handleError(throwable, "Error occurred while requesting buckets"));
        }
    }

    @Override
    public void loadMoreBuckets() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed() && loadNextBucketsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketsSubscription = bucketsController.getCurrentUserBuckets(pageNumber, BUCKETS_PER_PAGE_COUNT)
                    .toObservable()
                    .compose(RxTransformerUtils.executeRunnableIfObservableDidntEmitUntilGivenTime(
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

    @Override
    public void createBucket() {
        getView().showCreateBucketView();
    }

    private void setupRxBus() {
        busSubscription = rxBus.getEvents(BucketCreatedEvent.class)
                .compose(RxTransformerUtils.androidComputation())
                .subscribe(bucketCreatedEvent -> {
                    getView().addNewBucketOnTop(bucketCreatedEvent.getBucket());
                    getView().showBucketsList();
                    getView().scrollToTop();
                });
    }
}