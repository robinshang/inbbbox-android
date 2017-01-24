package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

@FragmentScope
public class RemoveFromBucketPresenter
        extends MvpNullObjectBasePresenter<RemoveFromBucketContract.View>
        implements RemoveFromBucketContract.Presenter {

    private static final int BUCKETS_PER_PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;
    private static final long REMOVE_DELAY = 500;

    private final BucketsController bucketsController;
    private final ErrorController errorController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketsSubscription;
    @NonNull
    private Subscription removeFromBucketSubscription;

    private Shot shot;
    private boolean apiHasMoreBuckets = true;
    private List<Bucket> bucketsToRemoveFromList;

    @Inject
    RemoveFromBucketPresenter(BucketsController bucketsController,
                              ErrorController errorController) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketsSubscription = Subscriptions.unsubscribed();
        removeFromBucketSubscription = Subscriptions.unsubscribed();
        bucketsToRemoveFromList = new ArrayList<>();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshSubscription.unsubscribe();
        loadNextBucketsSubscription.unsubscribe();
        removeFromBucketSubscription.unsubscribe();
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

    /**
     * Delay added to getting buckets shots to provide better UX experience
     * - when shot is added to only one bucket, removing will be preceded Immediately.
     * To avoid that dialog only "blink" there is delay set to fully show progress bar to the user
     */
    @Override
    public void loadBucketsForShot() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketsSubscription.unsubscribe();
            refreshSubscription = bucketsController.getListBucketsForShot(shot.id())
                    .delay(REMOVE_DELAY, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(getView()::showBucketListLoading)
                    .compose(RxTransformerUtil.applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::handleShotBuckets,
                            throwable -> handleError(throwable,
                                    "Error occurred while requesting buckets"));
        }
    }

    @Override
    public void loadMoreBuckets() {
        if (apiHasMoreBuckets && refreshSubscription.isUnsubscribed()
                && loadNextBucketsSubscription.isUnsubscribed()) {
            loadNextBucketsSubscription = bucketsController.getListBucketsForShot(shot.id())
                    .toObservable()
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showBucketListLoadingMore))
                    .compose(RxTransformers.androidIO())
                    .subscribe(buckets -> {
                        apiHasMoreBuckets = buckets.size() == BUCKETS_PER_PAGE_COUNT;
                        getView().showMoreBuckets(buckets);
                    }, throwable -> handleError(throwable,
                            "Error occurred while requesting more buckets"));
        }
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
    public void handleCheckboxClick(Bucket bucket, boolean isChecked) {
        if (isChecked) {
            bucketsToRemoveFromList.add(bucket);
        } else {
            removeBucketFromList(bucket);
        }
    }

    @Override
    public void removeShotFromBuckets() {
        getView().passResultAndCloseFragment(bucketsToRemoveFromList, shot);
    }

    private void handleShotBuckets(List<Bucket> buckets) {
        if (buckets.size() > 1) {
            showContainedBuckets(buckets);
        } else if (buckets.size() == 1) {
            removeImmediately(buckets.get(0));
        } else {
            getView().showMessageOnServerError(errorController
                    .getMessageBasedOnErrorCode(Constants.UNDEFINED));
        }
    }

    private void removeImmediately(Bucket bucket) {
        bucketsToRemoveFromList.add(bucket);
        getView().passResultAndCloseFragment(bucketsToRemoveFromList, shot);
    }

    private void showContainedBuckets(List<Bucket> buckets) {
        apiHasMoreBuckets = buckets.size() == BUCKETS_PER_PAGE_COUNT;
        getView().setBucketsList(buckets);
        getView().showBucketsList();
    }

    private void removeBucketFromList(Bucket bucketToRemove) {
        for (int i = 0; i < bucketsToRemoveFromList.size(); i++) {
            if (bucketsToRemoveFromList.get(i).id() == bucketToRemove.id())
                bucketsToRemoveFromList.remove(i);
        }
    }

}
