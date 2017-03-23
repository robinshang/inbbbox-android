package co.netguru.android.inbbbox.feature.shared.peekandpop;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.other.VibrationController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;

@FragmentScope
public class ShotPeekAndPopPresenter extends MvpNullObjectBasePresenter<ShotPeekAndPopContract.View>
        implements ShotPeekAndPopContract.Presenter {

    private static final int VIBRATE_DURATION_MS = 10;

    private final LikeShotController likeShotController;
    private final ErrorController errorController;
    private final BucketsController bucketsController;
    private final RxBus rxBus;
    private final VibrationController vibrationController;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private Shot shot;

    @Inject
    public ShotPeekAndPopPresenter(LikeShotController likeShotController,
                                   ErrorController errorController, RxBus rxBus,
                                   BucketsController bucketsController, Shot shot,
                                   VibrationController vibrationController) {
        this.bucketsController = bucketsController;
        this.likeShotController = likeShotController;
        this.errorController = errorController;
        this.rxBus = rxBus;
        this.shot = shot;
        this.vibrationController = vibrationController;
    }

    @Override
    public void addShotToBucket(Shot shot, Bucket bucket) {
        subscriptions.add(
                bucketsController.addShotToBucket(bucket.id(), shot)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(() -> onShotBucketedCompleted(shot),
                                throwable -> handleError(throwable, "Error while adding shot to bucket"))
        );
    }

    @Override
    public void toggleLikeShot() {
        if (!shot.isLiked()) {
            likeShot(shot);
        } else {
            unlikeShot(shot);
        }
    }

    @Override
    public void onBucketShot() {
        getView().showBucketChooserView(shot);
    }

    @Override
    public void vibrate() {
        vibrationController.vibrate(VIBRATE_DURATION_MS);
    }

    @Override
    public void detach() {
        subscriptions.clear();
    }

    private void likeShot(Shot shot) {
        subscriptions.add(likeShotController.likeShot(shot)
                .compose(applyCompletableIoSchedulers())
                .subscribe(() -> handleLikedShot(shot), this::handleLikeError));
    }

    private void unlikeShot(Shot shot) {
        subscriptions.add(likeShotController.unLikeShot(shot)
                .compose(applyCompletableIoSchedulers())
                .subscribe(() -> handleUnlikedShot(shot), this::handleLikeError));
    }

    private void handleLikedShot(Shot shot) {
        getView().showMessageShotLiked();
        rxBus.send(new ShotUpdatedEvent(updateShotLikeStatus(shot)));
    }

    private void handleUnlikedShot(Shot shot) {
        getView().showMessageShotUnliked();
        rxBus.send(new ShotUpdatedEvent(updateShotUnlikeStatus(shot)));
    }

    private void handleLikeError(Throwable throwable) {
        handleError(throwable, "Error while sending shot like");
    }

    private void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private Shot updateShotLikeStatus(Shot shot) {
        return Shot.update(shot)
                .likesCount(shot.likesCount() + 1)
                .isLiked(true)
                .build();
    }

    private Shot updateShotUnlikeStatus(Shot shot) {
        return Shot.update(shot)
                .likesCount(shot.likesCount() - 1)
                .isLiked(false)
                .build();
    }

    private void onShotBucketedCompleted(Shot shot) {
        getView().showBucketAddSuccess();
        rxBus.send(new ShotUpdatedEvent(updateShotBucketedStatus(shot)));
    }

    private Shot updateShotBucketedStatus(Shot shot) {
        return Shot.update(shot)
                .isBucketed(true)
                .build();
    }
}
