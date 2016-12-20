package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.CommentLoadMoreState;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applySingleIoSchedulers;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private final ShotDetailsController shotDetailsController;
    private final ErrorController errorController;
    private final CompositeSubscription subscriptions;
    private boolean isCommentModeInit;
    private Shot shot;
    private Comment commentInEditor;
    private CommentLoadMoreState commentLoadMoreState;
    private int pageNumber = 1;
    private int commentsCounter = 0;

    @Inject
    public ShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                ErrorController errorController) {
        this.shotDetailsController = shotDetailsController;
        this.errorController = errorController;
        this.subscriptions = new CompositeSubscription();
        this.commentLoadMoreState = new CommentLoadMoreState();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
        enableInputWhenIfInCommentMode();
        initializeView();

        downloadCommentsFromAPI();
    }

    @Override
    public void handleShotLike(boolean newLikeState) {
        subscriptions.add(
                shotDetailsController
                        .performLikeAction(shot, newLikeState)
                        .subscribe(() -> updateLikeState(newLikeState),
                                throwable -> handleError(throwable, "Error while performing like action"))
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
            getView().showSendingCommentIndicator();
            sendCommentToApi(comment);
        }
    }

    @Override
    public void onEditCommentClick(Comment currentComment) {
        commentInEditor = currentComment;
        getView().showCommentEditorDialog(currentComment.text());
    }

    @Override
    public void updateComment(String updatedComment) {
        subscriptions.add(
                shotDetailsController.updateComment(shot.id(),
                        commentInEditor.id(),
                        updatedComment)
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::handleCommentUpdated,
                                throwable -> handleError(throwable, "Error while updating comment"))
        );
    }

    @Override
    public void closeScreen() {
        getView().hideDetailsScreen();
    }

    @Override
    public void onCommentDelete(Comment currentComment) {
        commentInEditor = currentComment;
        getView().showDeleteCommentWarning();
    }

    @Override
    public void onCommentDeleteConfirmed() {
        subscriptions.add(
                shotDetailsController
                        .deleteComment(shot.id(), commentInEditor.id())
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(this::handleCommentDeleteComplete,
                                throwable -> handleError(throwable, "Error while deleting comment"))
        );
    }

    @Override
    public void getMoreComments() {
        pageNumber++;
        commentLoadMoreState.setLoadMoreActive(false);
        commentLoadMoreState.setWaitingForUpdate(true);
        getView().updateLoadMoreState(commentLoadMoreState);

        downloadCommentsFromAPI();
    }

    @Override
    public void onShotImageClick(List<Shot> allShots) {
        getView().openShotFullscreen(shot, allShots);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        getView().disableEditorProgressMode();
    }

    private void initializeView() {
        getView().showMainImage(shot);
        getView().updateLoadMoreState(commentLoadMoreState);
        getView().setInputShowingEnabled(false);
        showShotDetails(shot);
    }

    private void enableInputWhenIfInCommentMode() {
        if (isCommentModeInit) {
            getView().showInputIfHidden();
            getView().showKeyboard();
        }
    }

    private void handleCommentDeleteComplete() {
        getView().removeCommentFromView(commentInEditor);
        getView().showInfo(R.string.comment_deleted_complete);
    }

    private void sendCommentToApi(String comment) {
        subscriptions.add(
                shotDetailsController.sendComment(shot.id(), comment)
                        .compose(applySingleIoSchedulers())
                        .doAfterTerminate(this::handleSaveCommentTermination)
                        .subscribe(this::handleCommentSavingComplete,
                                throwable -> handleError(throwable, "Error while sending comment"))
        );
    }

    private void handleSaveCommentTermination() {
        getView().hideSendingCommentIndicator();
        getView().hideKeyboard();
    }

    private void handleCommentSavingComplete(Comment updatedComment) {
        getView().hideSendingCommentIndicator();
        getView().addNewComment(updatedComment);
        getView().clearCommentInput();
    }

    private void updateLikeState(boolean newLikeState) {
        shot = Shot.update(shot)
                .isLiked(newLikeState)
                .build();
        showShotDetails(shot);
    }

    private void handleDetailsStates(ShotDetailsState state) {
        List<Comment> comments = state.comments();
        commentsCounter += comments.size();

        getView().addCommentsToList(comments);
        updateLoadMoreState();

        updateShotDetails(state.isLiked(), state.isBucketed());

        checkCommentMode();
    }

    private void updateLoadMoreState() {
        commentLoadMoreState.setWaitingForUpdate(false);
        commentLoadMoreState.setLoadMoreActive(commentsCounter < shot.commentsCount());
        getView().updateLoadMoreState(commentLoadMoreState);
    }

    private void checkCommentMode() {
        if (isCommentModeInit) {
            getView().collapseAppbarWithAnimation();
            getView().scrollToLastItem();
        }
    }

    private void downloadCommentsFromAPI() {
        subscriptions.add(
                shotDetailsController.getShotComments(shot, pageNumber)
                        .compose(androidIO())
                        .doOnCompleted(() -> getView().setInputShowingEnabled(true))
                        .subscribe(this::handleDetailsStates,
                                throwable -> handleError(throwable, "Error while getting shot comments"))
        );
    }

    private void updateShotDetails(boolean liked, boolean bucketed) {
        if (shot.isLiked() != liked || shot.isBucketed() != bucketed) {
            shot = Shot.update(shot)
                    .isLiked(liked)
                    .isBucketed(bucketed)
                    .build();
            showShotDetails(shot);
        }
    }

    private void showShotDetails(Shot shotDetails) {
        Timber.d("Shot details received: %s", shotDetails);
        getView().showDetails(shotDetails);
        getView().initView();
    }

    private void handleCommentUpdated(Comment comment) {
        getView().dismissCommentEditor();
        getView().updateComment(commentInEditor, comment);
        getView().showInfo(R.string.comment_update_complete);
    }
}
