package co.netguru.android.inbbbox.feature.shot.detail;

import android.support.annotation.StringRes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.shot.model.ui.ShotImage;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface ShotDetailsContract {

    interface View extends MvpView, HttpErrorView {

        void showDetails(Shot shotDetails);

        void addCommentsToList(List<Comment> commentList);

        void showMainImage(ShotImage shotImage);

        void showMainImageWithGifAnimation(ShotImage shotImage);

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

        void requestFocusOnCommentInput();

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

        void openShotFullscreen(List<Shot> allShots, int previewShotIndex);

        void showBucketAddSuccess();

        void showShotRemoveFromBucketSuccess();

        void showAddShotToBucketView(Shot shot);

        void showRemoveShotFromBucketView(Shot shot);

        void showTeamView(UserWithShots userWithShots);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

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

        void onShotImageClick();

        void addShotToBucket(Bucket bucket, Shot shot);

        void removeShotFromBuckets(List<Bucket> list, Shot shot);

        void checkShotBucketsCount(Shot shot);

        void onShotBucketClicked(Shot shot);

        void getTeamUserWithShots(Team team);
    }
}
