package co.netguru.android.inbbbox.feature.details;

import android.support.annotation.StringRes;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.CommentLoadMoreState;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotImage;

public interface ShotDetailsContract {

    interface View extends BaseMvpRestView {

        void showDetails(Shot shotDetails);

        void addCommentsToList(List<Comment> commentList);

        void showMainImage(ShotImage shotImage);

        void initView();

        Shot getShotInitialData();

        String getCommentText();

        void setInputShowingEnabled(boolean b);

        boolean getCommentModeInitialState();

        void scrollToLastItem();

        void collapseAppbarWithAnimation();

        void showCommentEditorDialog(String text);

        void showKeyboard();

        void showInputIfHidden();

        void hideDetailsScreen();

        void hideKeyboard();

        void showSendingCommentIndicator();

        void hideSendingCommentIndicator();

        void addNewComment(Comment updatedComment);

        void clearCommentInput();

        void showDeleteCommentWarning();

        void showInfo(@StringRes int messageResId);

        void removeCommentFromView(Comment commentInEditor);

        void updateLoadMoreState(CommentLoadMoreState commentLoadMoreState);

        void dismissCommentEditor();

        void updateComment(Comment commentToUpdate, Comment updatedComment);

        void disableEditorProgressMode();
    }

    interface Presenter extends BaseMvpRestPresenter<ShotDetailsContract.View> {

        void downloadData();

        void handleShotLike(boolean newLikeState);

        void retrieveInitialData();

        void sendComment();

        void onEditCommentClick(Comment currentComment);

        void updateComment(String updatedComment);

        void closeScreen();

        void onCommentDelete(Comment currentComment);

        void onCommentDeleteConfirmed();

        void getMoreComments();
    }
}
