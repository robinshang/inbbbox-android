package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotImage;

public interface ShotDetailsContract {

    interface View extends MvpView {

        void showDetails(Shot shotDetails);

        void showComments(List<Comment> commentList);

        void showMainImage(ShotImage shotImage);

        void initView();

        void showErrorMessage(String errorMessageLabel);

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
    }

    interface Presenter extends MvpPresenter<View> {

        void downloadData();

        void handleShotLike(boolean newLikeState);

        void retrieveInitialData();

        void sendComment();

        void onEditCommentClick(Comment currentComment);

        void updateComment(String updatedComment);

        void closeScreen();
    }
}
