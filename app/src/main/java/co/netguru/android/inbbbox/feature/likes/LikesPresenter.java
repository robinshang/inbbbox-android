package co.netguru.android.inbbbox.feature.likes;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private static final int PAGE_COUNT = 30;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final LikedShotsController likedShotsController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextBucketSubscription;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    LikesPresenter(LikedShotsController likedShotsController) {
        this.likedShotsController = likedShotsController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextBucketSubscription.unsubscribe();
        }
    }

    @Override
    public void getLikesFromServer() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextBucketSubscription.unsubscribe();
            pageNumber = 1;
            refreshSubscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                    .toList()
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::onGetLikeShotListNext,
                            throwable -> Timber.e(throwable, "Error while getting likes from server"));
        }
    }

    @Override
    public void getMoreLikesFromServer() {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadNextBucketSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextBucketSubscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                    .compose(RxTransformerUtils.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreLikesView))
                    .toList()
                    .compose(androidIO())
                    .doAfterTerminate(() -> {
                        getView().hideProgressBar();
                        getView().hideLoadingMoreBucketsView();
                    })
                    .subscribe(this::onGetMoreLikeShotListNext,
                            throwable -> Timber.e(throwable, "Error while getting more likes from server"));
        }
    }

    @Override
    public void showShotDetails(Shot shot) {
        getView().openShowDetailsScreen(shot);
    }

    @Override
    public void checkDataEmpty(boolean isEmpty) {
        if (isEmpty) {
            getView().showEmptyLikesInfo();
        } else {
            getView().hideEmptyLikesInfo();

        }
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
}