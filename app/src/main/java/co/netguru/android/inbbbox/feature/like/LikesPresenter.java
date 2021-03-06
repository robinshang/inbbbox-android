package co.netguru.android.inbbbox.feature.like;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private static final int PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final LikeShotController likedShotsController;
    private final ErrorController errorController;
    private final RxBus rxBus;
    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadMoreLikesSubscription;
    @NonNull
    private Subscription busSubscription;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    LikesPresenter(LikeShotController likedShotsController,
                   ErrorController errorController,
                   RxBus rxBus) {
        this.likedShotsController = likedShotsController;
        this.errorController = errorController;
        this.rxBus = rxBus;
        refreshSubscription = Subscriptions.unsubscribed();
        loadMoreLikesSubscription = Subscriptions.unsubscribed();
        busSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void attachView(LikesViewContract.View view) {
        super.attachView(view);
        setupRxBus();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadMoreLikesSubscription.unsubscribe();
            busSubscription.unsubscribe();
        }
    }

    @Override
    public void getLikesFromServer() {
        if (refreshSubscription.isUnsubscribed()) {
            loadMoreLikesSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::onGetLikeShotListNext,
                            throwable -> handleError(throwable,
                                    "Error while getting likes from server"));
        }
    }

    @Override
    public void getMoreLikesFromServer() {
        if (hasMore && refreshSubscription.isUnsubscribed()
                && loadMoreLikesSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreLikesSubscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreLikesView))
                    .compose(androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBar();
                        getView().hideLoadingMoreLikesView();
                    })
                    .subscribe(this::onGetMoreLikeShotListNext,
                            throwable -> handleError(throwable,
                                    "Error while getting more likes from server"));
        }
    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> allShots) {
        getView().openShowDetailsScreen(shot, allShots);
    }

    @Override
    public void checkDataEmpty(boolean isEmpty) {
        if (isEmpty) {
            getView().showEmptyLikesInfo();
        } else {
            getView().hideEmptyLikesInfo();
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onGetLikeShotListNext(List<Shot> likedShotList) {
        hasMore = likedShotList.size() == PAGE_COUNT;
        getView().setData(likedShotList);
        getView().showContent();
    }

    private void onGetMoreLikeShotListNext(List<Shot> likedShotList) {
        hasMore = likedShotList.size() == PAGE_COUNT;
        getView().showMoreLikes(likedShotList);
    }

    private void setupRxBus() {
        busSubscription = rxBus.getEvents(ShotUpdatedEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(shotLikedEvent -> {
                    if (shotLikedEvent.getShot().isLiked()) {
                        getView().showLikeAtTop(shotLikedEvent.getShot());
                    } else {
                        getView().removeShotFromLikes(shotLikedEvent.getShot());
                    }
                });
    }
}