package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.controler.ShotsPagingController;
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

    private final ShotsController shotsController;
    private final ShotsPagingController shotsPagingController;
    private final ErrorMessageController errorMessageController;
    private final LikeShotController likeShotController;
    private final BucketsController bucketsController;
    private final CompositeSubscription subscriptions;

    @Inject
    ShotsPresenter(ShotsController shotsController, ShotsPagingController shotsPagingController,
                   ErrorMessageController errorMessageController, LikeShotController likeShotController,
                   BucketsController bucketsController) {

        this.shotsController = shotsController;
        this.shotsPagingController = shotsPagingController;
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
        return Shot.builder()
                .id(shot.id())
                .title(shot.title())
                .author(shot.author())
                .team(shot.team())
                .projectUrl(shot.projectUrl())
                .creationDate(shot.creationDate())
                .likesCount(shot.likesCount())
                .bucketCount(shot.bucketCount())
                .description(shot.description())
                .isGif(shot.isGif())
                .hdpiImageUrl(shot.hdpiImageUrl())
                .normalImageUrl(shot.normalImageUrl())
                .thumbnailUrl(shot.thumbnailUrl())
                .creationDate(shot.creationDate())
                .author(shot.author())
                .isGif(shot.isGif())
                .isLiked(true)
                .isBucketed(shot.isBucketed())
                .build();
    }

    @Override
    public void getShotsFromServer() {
        shotsPagingController.setFirstShotsPage();
        final Subscription subscription = shotsController.getShots(shotsPagingController.getShotsPage(),
                ShotsPagingController.SHOTS_PER_PAGE)
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
        subscriptions.add(subscription);
    }

    @Override
    public void getMoreShotsFromServer() {
        if (shotsPagingController.hasMore()) {
            shotsPagingController.setNextShotsPage();
            final Subscription subscription = shotsController.getShots(shotsPagingController.getShotsPage(),
                    ShotsPagingController.SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .subscribe(this::showMoreRetrievedItems,
                            this::handleException);
            subscriptions.add(subscription);
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

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        if (shotsList.size() < ShotsPagingController.SHOTS_PER_PAGE) {
            shotsPagingController.changeHasMoreToFalse();
        }
        getView().showItems(shotsList);
        getView().hideLoadingIndicator();
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().hideLoadingIndicator();
        getView().showError(errorMessageController.getErrorMessageLabel(exception));
    }

    private void showMoreRetrievedItems(List<Shot> shotList) {
        Timber.d("More shots received!");
        if (shotList.size() < ShotsPagingController.SHOTS_PER_PAGE) {
            shotsPagingController.changeHasMoreToFalse();
        }
        getView().showMoreItems(shotList);
        getView().hideLoadingIndicator();
    }
}
