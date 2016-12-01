package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.R.attr.id;
import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.utils.StringUtils.PARAGRAPH_TAG_END;
import static co.netguru.android.inbbbox.utils.StringUtils.PARAGRAPH_TAG_START;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;
    private static final int PAGE_NUMBER = 1;

    private final ShotDetailsController shotDetailsController;
    private final ErrorMessageController errorMessageController;
    private final UserShotsController userShotsController;
    private final CompositeSubscription subscriptions;
    private boolean isCommentModeInit;
    private Shot shot;
    private Comment commentInEditor;

    @Inject
    public ShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                ErrorMessageController messageController,
                                UserShotsController userShotsController) {
        this.shotDetailsController = shotDetailsController;
        this.errorMessageController = messageController;
        this.userShotsController = userShotsController;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
        enableInputWhenIfInCommentMode();
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

    private void enableInputWhenIfInCommentMode() {
        if (isCommentModeInit) {
            getView().showInputIfHidden();
            getView().showKeyboard();
        }
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
        this.isCommentModeInit = getView().getCommentModeInitialState();
    }

    @Override
    public void sendComment() {
        String comment = getView().getCommentText();
        if (!StringUtils.isBlank(comment)) {
            sendCommentToApi(comment);
        }
    }

    @Override
    public void openCommentEditor(Comment currentComment) {
        commentInEditor = currentComment;
        getView().showCommentEditorDialog(
                currentComment.text()
                        .replace(PARAGRAPH_TAG_START, "")
                        .replace(PARAGRAPH_TAG_END, ""));
    }

    @Override
    public void updateComment(String updatedComment) {
        // TODO: 28.11.2016 no in scope of this task
    }

    @Override
    public void closeScreen() {
        getView().hideDetailsScreen();
    }

    @Override
    public void downloadUserShots(User user) {
        final Subscription subscription = userShotsController.getUserShotsList(id,
                PAGE_NUMBER, SHOT_PAGE_COUNT)
                .compose(androidIO())
                .subscribe(list -> createFollower(user, list),
                        throwable -> Timber.e(throwable, "Error while getting user shots"));
        subscriptions.add(subscription);
    }

    private void createFollower(User user, List<Shot> list) {
        getView().showUserDetails(Follower.createFromUser(user, list));
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
        checkCommentMode();
    }

    private void checkCommentMode() {
        if (isCommentModeInit) {
            getView().collapseAppbarWithAnimation();
            getView().scrollToLastItem();
        }
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
