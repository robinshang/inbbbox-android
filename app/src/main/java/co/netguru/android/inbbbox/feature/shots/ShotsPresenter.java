package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applyCompletableIoSchedulers;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private static final int FIRST_PAGE = 1;

    private final ShotsController shotsController;
    private final ErrorMessageController errorMessageController;
    private final LikeShotController likeShotController;
    private final BucketsController bucketsController;
    private final CompositeSubscription subscriptions;

    private int pageNumber = FIRST_PAGE;
    private boolean hasMore = true;

    @Inject
    ShotsPresenter(ShotsController shotsController, ErrorMessageController errorMessageController, LikeShotController likeShotController,
                   BucketsController bucketsController) {

        this.shotsController = shotsController;
        this.errorMessageController = errorMessageController;
        this.likeShotController = likeShotController;
        this.bucketsController = bucketsController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void likeShot(Shot shot) {
        getView().closeFabMenu();
        if (!shot.isLiked()) {
            final Subscription subscription = likeShotController.likeShot(shot.id())
                    .compose(applyCompletableIoSchedulers())
                    .subscribe(() -> onShotLikeCompleted(shot), this::onShotLikeError);
            subscriptions.add(subscription);
        }
    }

    private void onShotLikeCompleted(Shot shot) {
        Timber.d("Shot liked : %s", shot);
        getView().changeShotLikeStatus(changeShotLikeStatus(shot));
    }

    private void onShotLikeError(Throwable throwable) {
        Timber.e(throwable, "Error while sending shot like");
        getView().showError(errorMessageController.getErrorMessageLabel(throwable));
    }

    private Shot changeShotLikeStatus(Shot shot) {
        return Shot.update(shot)
                .isLiked(true)
                .build();
    }

    @Override
    public void getShotsFromServer() {
        pageNumber = FIRST_PAGE;
        getView().showLoadingIndicator();
        getShots();
    }

    @Override
    public void getMoreShotsFromServer() {
        if (hasMore) {
            pageNumber++;
            getShots();
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
                bucketsController.addShotToBucket(bucket.id(), shot.id())
                        .compose(RxTransformerUtils.applyCompletableIoSchedulers())
                        .subscribe(
                                getView()::showBucketAddSuccess,
                                throwable -> {
                                    Timber.d(throwable, "Error while adding shot to bucket");
                                    getView().showError(throwable.getMessage());
                                })
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

    private void getShots() {
        final Subscription subscription = shotsController.getShots(pageNumber, SHOTS_PER_PAGE)
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
        subscriptions.add(subscription);
    }

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        hasMore = shotsList.size() >= SHOTS_PER_PAGE;
        getView().hideLoadingIndicator();

        if (pageNumber == FIRST_PAGE) {
            getView().showItems(shotsList);
        } else {
            getView().showMoreItems(shotsList);
        }
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().hideLoadingIndicator();
        getView().showError(errorMessageController.getErrorMessageLabel(exception));
    }
}
