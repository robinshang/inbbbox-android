package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
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
    private final ErrorMessageController errorMessageController;
    private final CompositeSubscription subscriptions;
    private boolean isCommentModeInit;
    private Shot shot;
    private Comment commentInEditor;
    private CommentLoadMoreState commentLoadMoreState;
    private int pageNumber = 1;
    private int commentsCounter = 0;

    @Inject
    public ShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                ErrorMessageController messageController) {
        this.shotDetailsController = shotDetailsController;
        this.errorMessageController = messageController;
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
                                this::handleApiError)
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
                                this::handleApiError)
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
                                this::handleApiError)
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
                shotDetailsController.getShotComments(shot.id(), pageNumber)
                        .compose(androidIO())
                        .doOnCompleted(() -> getView().setInputShowingEnabled(true))
                        .subscribe(this::handleDetailsStates, this::handleApiError)
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

    private void handleApiError(Throwable throwable) {
        Timber.e(throwable, "details download failed! ");
        getView().showErrorMessage(errorMessageController.getErrorMessageLabel(throwable));
        getView().disableEditorProgressMode();
    }
}
