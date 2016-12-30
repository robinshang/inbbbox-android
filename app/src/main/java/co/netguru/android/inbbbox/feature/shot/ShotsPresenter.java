package co.netguru.android.inbbbox.feature.shot;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private static final int FIRST_PAGE = 1;

    private final ShotsController shotsController;
    private final ErrorController errorController;
    private final LikeShotController likeShotController;
    private final BucketsController bucketsController;
    private final CompositeSubscription subscriptions;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadMoreSubscription;
    private int pageNumber = FIRST_PAGE;
    private boolean hasMore = true;

    @Inject
    ShotsPresenter(ShotsController shotsController, LikeShotController likeShotController,
                   BucketsController bucketsController, ErrorController errorController) {

        this.shotsController = shotsController;
        this.likeShotController = likeShotController;
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        subscriptions = new CompositeSubscription();
        refreshSubscription = Subscriptions.unsubscribed();
        loadMoreSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadMoreSubscription.unsubscribe();
        }
    }

    @Override
    public void likeShot(Shot shot) {
        getView().closeFabMenu();
        if (!shot.isLiked()) {
            final Subscription subscription = likeShotController.likeShot(shot)
                    .compose(applyCompletableIoSchedulers())
                    .subscribe(() -> onShotLikeCompleted(shot),
                            throwable -> handleError(throwable, "Error while sending shot like"));
            subscriptions.add(subscription);
        }
    }

    @Override
    public void getShotsFromServer(boolean pullToRefresh) {
        if (refreshSubscription.isUnsubscribed()) {
            loadMoreSubscription.unsubscribe();
            pageNumber = FIRST_PAGE;
            getView().showLoadingIndicator(pullToRefresh);
            refreshSubscription = shotsController.getShots(pageNumber, SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        Timber.d("Shots received!");
                        hasMore = shotList.size() >= SHOTS_PER_PAGE;
                        getView().hideLoadingIndicator();
                        getView().setData(shotList);
                        getView().showContent();
                    }, throwable -> handleError(throwable, "Error while getting shots"));
        }
    }

    @Override
    public void getMoreShotsFromServer() {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadMoreSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreSubscription = shotsController.getShots(pageNumber, SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        Timber.d("Shots received!");
                        hasMore = shotList.size() >= SHOTS_PER_PAGE;
                        getView().hideLoadingIndicator();
                        getView().showMoreItems(shotList);
                    }, throwable -> handleError(throwable, "Error while getting more shots"));
        }
    }

    @Override
    public void handleAddShotToBucket(Shot shot) {
        getView().closeFabMenu();
        getView().showBucketChoosing(shot);
    }

    @Override
    public void addShotToBucket(Bucket bucket, Shot shot) {
        subscriptions.add(
                bucketsController.addShotToBucket(bucket.id(), shot)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(
                                getView()::showBucketAddSuccess,
                                throwable -> handleError(throwable, "Error while adding shot to bucket"))
        );
    }

    @Override
    public void showShotDetails(Shot shot) {
        getView().showShotDetails(shot);
    }

    @Override
    public void showCommentInput(Shot selectedShot) {
        getView().closeFabMenu();
        getView().showDetailsScreenInCommentMode(selectedShot);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().hideLoadingIndicator();
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onShotLikeCompleted(Shot shot) {
        Timber.d("Shot liked : %s", shot);
        getView().changeShotLikeStatus(changeShotLikeStatus(shot));
    }

    private Shot changeShotLikeStatus(Shot shot) {
        return Shot.update(shot)
                .isLiked(true)
                .build();
    }
}