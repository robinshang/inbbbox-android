package co.netguru.android.inbbbox.feature.details;

import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private final ShotDetailsController shotDetailsController;
    private final ErrorMessageController errorMessageController;
    private final CompositeSubscription subscriptions;
    private Shot shot;

    @Inject
    public ShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                ErrorMessageController messageController) {
        this.shotDetailsController = shotDetailsController;
        this.errorMessageController = messageController;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
        getView().showMainImage(shot);
        getView().setInputShowingEnabled(false);
        showShotDetails(shot);
        subscriptions.add(
                shotDetailsController.getShotComments(shot.id())
                        .compose(androidIO())
                        .doOnCompleted(() -> getView().setInputShowingEnabled(true))
                        .subscribe(this::handleDetailsStates, this::handleApiError)
        );
    }

    @Override
    public void handleShotLike(boolean newLikeState) {
        subscriptions.add(
                shotDetailsController
                        .performLikeAction(shot.id(), newLikeState)
                        .subscribe(() -> updateLikeState(newLikeState),
                                this::handleApiError)
        );
    }

    @Override
    public void retrieveInitialData() {
        this.shot = getView().getShotInitialData();
    }

    @Override
    public void sendComment() {
        String comment = getView().getCommentText();
        if (!StringUtils.isBlank(comment)) {
            sendCommentToApi(comment);
        }
    }

    private void sendCommentToApi(String comment) {
        // TODO: 23.11.2016 not in scope of this task
    }

    private void updateLikeState(boolean newLikeState) {
        shot = Shot.update(shot)
                .isLiked(newLikeState)
                .build();
        showShotDetails(shot);
    }

    private void handleDetailsStates(ShotDetailsState state) {
        getView().showComments(state.comments());
        updateShotDetails(state.isLiked(), state.isBucketed());
        showShotDetails(shot);
    }

    private void updateShotDetails(boolean liked, boolean bucketed) {
        shot = Shot.update(shot)
                .isLiked(liked)
                .isBucketed(bucketed)
                .build();
    }

    private void showShotDetails(Shot shotDetails) {
        Timber.d("Shot details received: %s", shotDetails);
        getView().showDetails(shotDetails);
        getView().initView();
    }

    private void handleApiError(Throwable throwable) {
        Timber.e(throwable, "details download failed! ");
        getView().showErrorMessage(errorMessageController.getErrorMessageLabel(throwable));
    }

}
