package co.netguru.android.inbbbox.feature.shared.peekandpop;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
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

    private final LikeShotController likeShotController;
    private final ErrorController errorController;
    private final RxBus rxBus;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    public ShotPeekAndPopPresenter(LikeShotController likeShotController,
                                   ErrorController errorController, RxBus rxBus) {
        this.likeShotController = likeShotController;
        this.errorController = errorController;
        this.rxBus = rxBus;
    }

    @Override
    public void toggleLikeShot(Shot shot) {
        if(!shot.isLiked()) {
            likeShot(shot);
        } else {
            unlikeShot(shot);
        }
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

    @Override
    public void detach() {
        subscriptions.clear();
    }

    private void handleUnlikedShot(Shot shot) {
        rxBus.send(new ShotUpdatedEvent(updateShotUnlikeStatus(shot)));
    }

    private void handleLikedShot(Shot shot) {
        rxBus.send(new ShotUpdatedEvent(updateShotLikeStatus(shot)));
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
}
