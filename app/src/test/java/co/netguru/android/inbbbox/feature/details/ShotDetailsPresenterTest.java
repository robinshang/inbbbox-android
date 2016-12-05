package co.netguru.android.inbbbox.feature.details;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.CommentLoadMoreState;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotDetailsPresenterTest {

    private static final long EXAMPLE_ID = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Shot shotMock;

    @Mock
    ShotDetailsController shotDetailsControllerMock;

    @Mock
    ErrorMessageController errorMessageControllerMock;

    @Mock
    UserShotsController userShotsControllerMock;

    @Mock
    ShotDetailsContract.View viewMock;

    @Mock
    User userMock;

    @InjectMocks
    ShotDetailsPresenter shotDetailsPresenter;

    private int requestedPage = 1;

    @Before
    public void setUp() {
        shotDetailsPresenter.attachView(viewMock);
        when(shotMock.id()).thenReturn(EXAMPLE_ID);
        when(shotMock.author()).thenReturn(User.create(Statics.USER_ENTITY));
        when(shotMock.creationDate()).thenReturn(LocalDateTime.now());
        when(viewMock.getShotInitialData()).thenReturn(shotMock);
        when(errorMessageControllerMock.getErrorMessageLabel(any(Throwable.class))).thenCallRealMethod();
        shotDetailsPresenter.retrieveInitialData();
    }

    @Test
    public void whenShotDetailsDownload_thenShowMainShotImageWithShotImageInterface() {
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID, requestedPage)).thenReturn(Observable.empty());

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showMainImage(shotMock);
    }

    @Test
    public void whenShotDetailsDownload_thenShowDetailsWithShotData() {
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID, requestedPage)).thenReturn(Observable.empty());

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(shotMock);
        verify(viewMock, times(1)).initView();
    }

    @Test
    public void whenShotDetailsDownload_thenLoadDetailsAndUpdateItAfterStateAreDownloaded() {
        boolean expectedLikeState = true;
        ShotDetailsState resultState = ShotDetailsState.create(expectedLikeState, false, Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID, requestedPage)).thenReturn(Observable.just(resultState));
        when(shotMock.isLiked()).thenReturn(expectedLikeState);
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(expectedLikeState, shot.isLiked());
        verify(viewMock, times(1)).initView();
    }

    @Test
    public void whenShotDetailsDownload_thenShowShotNotLiked() {
        boolean expectedLikeState = false;
        ShotDetailsState resultState = ShotDetailsState.create(expectedLikeState, false, Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID, requestedPage)).thenReturn(Observable.just(resultState));
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(shot.isLiked(), expectedLikeState);
        verify(viewMock, times(1)).initView();
    }

    @Test
    public void whenShotLiked_thenPerformLikeActionWithTrue() {
        boolean likeState = true;
        when(shotDetailsControllerMock.performLikeAction(EXAMPLE_ID, likeState))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.handleShotLike(likeState);

        verify(shotDetailsControllerMock, times(1)).performLikeAction(EXAMPLE_ID, likeState);
    }

    @Test
    public void whenShotLiked_thenPerformLikeActionWithFalse() {
        boolean likeState = false;
        when(shotDetailsControllerMock.performLikeAction(EXAMPLE_ID, likeState))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.handleShotLike(likeState);

        verify(shotDetailsControllerMock, times(1)).performLikeAction(EXAMPLE_ID, likeState);
    }

    @Test
    public void whenShotLiked_thenUpdateUserDetails() {
        boolean likeState = false;
        when(shotDetailsControllerMock.performLikeAction(anyLong(), anyBoolean()))
                .thenReturn(Completable.complete());
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.handleShotLike(likeState);

        verify(viewMock, times(1)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(shot.isLiked(), likeState);
        verify(viewMock, times(1)).initView();
    }

    @Test
    public void whenInputModeEnabled_thenShowKeyboardAndShowInputOnDataDownload() {
        when(viewMock.getCommentModeInitialState()).thenReturn(true);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt())).thenReturn(Observable.empty());

        shotDetailsPresenter.retrieveInitialData();
        shotDetailsPresenter.downloadData();

        verify(viewMock, atLeastOnce()).showKeyboard();
        verify(viewMock, atLeastOnce()).showInputIfHidden();
    }

    @Test
    public void whenShotDetailsDownloadComplete_thenEnableInputShow() {
        when(viewMock.getCommentModeInitialState()).thenReturn(true);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.empty());

        shotDetailsPresenter.retrieveInitialData();
        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).setInputShowingEnabled(true);
    }

    @Test
    public void whenInputModeEnabled_thenAutoScrollViewAllDetailsAreReady() {
        when(viewMock.getCommentModeInitialState()).thenReturn(true);
        when(shotDetailsControllerMock
                .getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(mock(ShotDetailsState.class)));

        shotDetailsPresenter.retrieveInitialData();
        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).collapseAppbarWithAnimation();
        verify(viewMock, times(1)).scrollToLastItem();
    }

    @Test
    public void whenCloseScreenIsCalled_thenHideDetailsScreen() {

        shotDetailsPresenter.closeScreen();

        verify(viewMock, times(1)).hideDetailsScreen();
    }

    @Test
    public void whenSendCommentToApiCalled_thenGetCommentsTextFromUI() {

        shotDetailsPresenter.sendComment();

        verify(viewMock, times(1)).getCommentText();
    }

    @Test
    public void whenSendCommentClickedAndCommentIsEmpty_thenDoNothing() {
        String exampleComment = "";
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.just(mock(Comment.class)));

        shotDetailsPresenter.sendComment();

        verify(shotDetailsControllerMock, never()).sendComment(anyLong(), anyString());
    }

    @Test
    public void whenSendCommentClickedAndCommentIsNotEmpty_thenShowSendCommentIndicator() {
        String exampleComment = "test";
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.just(mock(Comment.class)));

        shotDetailsPresenter.sendComment();

        verify(viewMock, times(1)).showSendingCommentIndicator();
    }

    @Test
    public void whenSendCommentToApiSend_thenGetSendCommentObservable() {
        String exampleComment = "test";
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.just(mock(Comment.class)));

        shotDetailsPresenter.sendComment();

        verify(shotDetailsControllerMock, times(1)).sendComment(anyLong(), eq(exampleComment));
    }

    @Test
    public void whenSendCommentIsSuccess_thenHideKeyboardClearInputAndAddNewCommentToView() {
        String exampleComment = "test";
        Comment expectedComment = mock(Comment.class);
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.just(expectedComment));

        shotDetailsPresenter.sendComment();

        verify(viewMock, times(1)).hideKeyboard();
        verify(viewMock, times(1)).clearCommentInput();
        verify(viewMock, times(1)).addNewComment(expectedComment);
    }

    @Test
    public void whenSendCommentSuccess_thenHideSendingIndicatorOnActionEnd() {
        String exampleComment = "test";
        Comment expectedComment = mock(Comment.class);
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.just(expectedComment));

        shotDetailsPresenter.sendComment();

        verify(viewMock, atLeastOnce()).hideSendingCommentIndicator();
    }

    @Test
    public void whenSendCommendFailed_thenHideSendingIndicatorOnActionEnd() {
        String exampleComment = "test";
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.error(new Throwable("test")));

        shotDetailsPresenter.sendComment();

        verify(viewMock, atLeastOnce()).hideSendingCommentIndicator();
    }

    @Test
    public void whenEditCommentClick_thenShowCommentEditor() {
        Comment exampleComment = Statics.COMMENTS.get(0);
        String expectedText = exampleComment.text();

        shotDetailsPresenter.onEditCommentClick(exampleComment);

        verify(viewMock, times(1)).showCommentEditorDialog(expectedText);
    }

    @Test
    public void whenCommentDeleteSelected_thenShowDeleteCommentWarning() {
        Comment commentMock = Statics.COMMENTS.get(0);

        shotDetailsPresenter.onCommentDelete(commentMock);

        verify(viewMock, times(1)).showDeleteCommentWarning();
    }

    @Test
    public void whenCommentDeletionConfirmed_thenGetDeleteCommentComplete() {
        Comment commentMock = Statics.COMMENTS.get(0);
        shotDetailsPresenter.onCommentDelete(commentMock);
        when(shotDetailsControllerMock.deleteComment(anyLong(), anyLong()))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.onCommentDeleteConfirmed();

        verify(shotDetailsControllerMock, times(1)).deleteComment(shotMock.id(), commentMock.id());
    }

    @Test
    public void whenCommentDeletionComplete_thenRemoveSelectedCommentFromViewAndShowInfo() {
        Comment commentMock = Statics.COMMENTS.get(0);
        shotDetailsPresenter.onCommentDelete(commentMock);
        when(shotDetailsControllerMock.deleteComment(anyLong(), anyLong()))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.onCommentDeleteConfirmed();

        verify(viewMock, times(1)).removeCommentFromView(commentMock);
        verify(viewMock, times(1)).showInfo(anyInt());
    }

    @Test
    public void whenGetMoreCommentsCalled_thenIncreaseRequestedPageCount() {
        //page 1 is set initially
        int pagingStart = 2;
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.empty());

        for (int i = pagingStart; i < 200; i++) {
            shotDetailsPresenter.getMoreComments();

            verify(shotDetailsControllerMock).getShotComments(anyLong(), eq(i));
        }
    }

    @Test
    public void whenGetMoreCommentsCalled_thenChangeLoadMoreStateButtonToInactiveAndWaitingForUpdateTrue() {
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.empty());
        ArgumentCaptor<CommentLoadMoreState> argumentCaptor = ArgumentCaptor
                .forClass(CommentLoadMoreState.class);

        shotDetailsPresenter.getMoreComments();

        verify(viewMock).updateLoadMoreState(argumentCaptor.capture());
        Assert.assertEquals(true, argumentCaptor.getValue().isWaitingForUpdate());
        Assert.assertEquals(false, argumentCaptor.getValue().isLoadMoreActive());
    }

    @Test
    public void whenGetMoreCommentsCompleted_thenAddNewCommentsToView() {
        ShotDetailsState shotDetailsStateMock = mock(ShotDetailsState.class);
        when(shotDetailsStateMock.comments()).thenReturn(Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(shotDetailsStateMock));

        shotDetailsPresenter.getMoreComments();

        verify(viewMock, times(1)).addCommentsToList(Statics.COMMENTS);
    }

    @Test
    public void whenGetMoreCommentsCompletedAndAllCommentsAreLoaded_thenUpdatedLoadMoreItemStateToActive() {
        ShotDetailsState shotDetailsStateMock = mock(ShotDetailsState.class);
        when(shotDetailsStateMock.comments()).thenReturn(Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(shotDetailsStateMock));
        when(shotMock.commentsCount()).thenReturn(Statics.COMMENTS.size() + 5);
        ArgumentCaptor<CommentLoadMoreState> argumentCaptor = ArgumentCaptor
                .forClass(CommentLoadMoreState.class);

        shotDetailsPresenter.getMoreComments();

        verify(viewMock, times(2)).updateLoadMoreState(argumentCaptor.capture());
        List<CommentLoadMoreState> values = argumentCaptor.getAllValues();
        Assert.assertEquals(false, values.get(1).isWaitingForUpdate());
        Assert.assertEquals(true, values.get(1).isLoadMoreActive());
    }

    @Test
    public void whenGetMoreCommentsCompletedAndNotAllCommentsAreLoaded_thenUpdatedLoadMoreItemStateToActive() {
        ShotDetailsState shotDetailsStateMock = mock(ShotDetailsState.class);
        when(shotDetailsStateMock.comments()).thenReturn(Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(shotDetailsStateMock));
        when(shotMock.commentsCount()).thenReturn(Statics.COMMENTS.size());
        ArgumentCaptor<CommentLoadMoreState> argumentCaptor = ArgumentCaptor
                .forClass(CommentLoadMoreState.class);

        shotDetailsPresenter.getMoreComments();

        verify(viewMock, times(2)).updateLoadMoreState(argumentCaptor.capture());
        List<CommentLoadMoreState> values = argumentCaptor.getAllValues();
        Assert.assertEquals(false, values.get(1).isWaitingForUpdate());
        Assert.assertEquals(false, values.get(1).isLoadMoreActive());
    }

    @Test
    public void whenLikedStateIsDifferentThanCurrent_thenUpdatedShotDetails() {
        ShotDetailsState shotDetailsStateMock = mock(ShotDetailsState.class);
        boolean isLiked = true;
        boolean isBucketed = false;
        when(shotMock.isLiked()).thenReturn(isLiked);
        when(shotMock.isBucketed()).thenReturn(isBucketed);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(shotDetailsStateMock));
        when(shotMock.commentsCount()).thenReturn(Statics.COMMENTS.size());
        when(shotDetailsStateMock.isLiked()).thenReturn(!isLiked);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(2)).showDetails(any(Shot.class));
    }

    @Test
    public void whenBucketStateIsDifferentThanCurrent_thenUpdatedShotDetails() {
        ShotDetailsState shotDetailsStateMock = mock(ShotDetailsState.class);
        boolean isLiked = true;
        boolean isBucketed = false;
        when(shotMock.isLiked()).thenReturn(isLiked);
        when(shotMock.isBucketed()).thenReturn(isBucketed);
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.just(shotDetailsStateMock));
        when(shotMock.commentsCount()).thenReturn(Statics.COMMENTS.size());
        when(shotDetailsStateMock.isBucketed()).thenReturn(!isLiked);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(2)).showDetails(any(Shot.class));
    }

    @Test
    public void whenOnEditCommentClick_thenShowCommentEditor() {
        Comment comment = Statics.COMMENTS.get(0);
        String exampleCommentText = comment.text();

        shotDetailsPresenter.onEditCommentClick(comment);

        verify(viewMock, times(1)).showCommentEditorDialog(exampleCommentText);
    }

    @Test
    public void whenUpdateCommentConfirmed_thenSendUpdateRequestWithCommentIdShotIdAndNewValue() {
        Comment comment = Statics.COMMENTS.get(0);
        when(shotDetailsControllerMock.updateComment(anyLong(), anyLong(), anyString()))
                .thenReturn(Single.just(comment));
        long commentId = comment.id();
        long shotId = shotMock.id();
        shotDetailsPresenter.onEditCommentClick(comment);
        String expectedCommentUpdate = "test";

        shotDetailsPresenter.updateComment(expectedCommentUpdate);

        verify(shotDetailsControllerMock, times(1))
                .updateComment(shotId, commentId, expectedCommentUpdate);
    }

    @Test
    public void whenUpdateCommentConfirmed_thenReplaceNewComment() {
        Comment comment = Statics.COMMENTS.get(0);
        when(shotDetailsControllerMock.updateComment(anyLong(), anyLong(), anyString()))
                .thenReturn(Single.just(comment));
        shotDetailsPresenter.onEditCommentClick(comment);
        String expectedCommentUpdate = "test";

        shotDetailsPresenter.updateComment(expectedCommentUpdate);

        verify(viewMock, times(1)).updateComment(comment, comment);
    }

    @Test
    public void whenUpdateCommentComplete_thenCloseCommentEditorAndShowCommentUpdateCompleteInfo() {
        Comment comment = Statics.COMMENTS.get(0);
        when(shotDetailsControllerMock.updateComment(anyLong(), anyLong(), anyString()))
                .thenReturn(Single.just(comment));
        shotDetailsPresenter.onEditCommentClick(comment);
        String expectedCommentUpdate = "test";

        shotDetailsPresenter.updateComment(expectedCommentUpdate);

        verify(viewMock, times(1)).dismissCommentEditor();
        verify(viewMock, times(1)).showInfo(R.string.comment_update_complete);
    }

    @Test
    public void whenUserSelected_thenDownloadUserUsingUserController() {
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.empty());
        when(userMock.id()).thenReturn(EXAMPLE_ID);

        shotDetailsPresenter.downloadUserShots(userMock);

        verify(userShotsControllerMock, times(1))
                .getUserShotsList(eq(EXAMPLE_ID), anyInt(), anyInt());
    }

    @Test
    public void whenUserAccountSelectedAndUserDataDowloadComplete_thenShowUserDetails() {
        User exampleUser = User.create(Statics.USER_ENTITY);
        List<Shot> listOfShots = Arrays.asList(Statics.LIKED_SHOT, Statics.NOT_LIKED_SHOT);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.just(listOfShots));
        ArgumentCaptor<Follower> argumentCaptor = ArgumentCaptor.forClass(Follower.class);

        shotDetailsPresenter.downloadUserShots(exampleUser);

        verify(viewMock, times(1)).showUserDetails(argumentCaptor.capture());
        Assert.assertEquals(EXAMPLE_ID, argumentCaptor.getValue().id());
        Assert.assertEquals(listOfShots, argumentCaptor.getValue().shotList());
    }

    //ERRORS
    @Test
    public void whenUserAccountSelectedAndUserDataDownloadFailed_thenShowError() {
        String message = "test";
        Throwable throwable = new Throwable(message);
        User exampleUser = User.create(Statics.USER_ENTITY);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.error(throwable));

        shotDetailsPresenter.downloadUserShots(exampleUser);

        verify(viewMock, times(1)).showErrorMessage(message);
    }

    @Test
    public void whenUpdateCommentFailed_thenShowInfoAndDisableProgressModeInCommentEditor() {
        Comment comment = Statics.COMMENTS.get(0);
        when(shotDetailsControllerMock.updateComment(anyLong(), anyLong(), anyString()))
                .thenReturn(Single.error(new Throwable("test")));
        shotDetailsPresenter.onEditCommentClick(comment);
        String expectedCommentUpdate = "test";

        shotDetailsPresenter.updateComment(expectedCommentUpdate);

        verify(viewMock, times(1))
                .disableEditorProgressMode();
    }

    @Test
    public void whenCommentDeletionFailed_thenRemoveSelectedCommentFromView() {
        Comment commentMock = Statics.COMMENTS.get(0);
        shotDetailsPresenter.onCommentDelete(commentMock);
        when(shotDetailsControllerMock.deleteComment(anyLong(), anyLong()))
                .thenReturn(Completable.error(new Throwable("test")));

        shotDetailsPresenter.onCommentDeleteConfirmed();

        verify(viewMock, times(1)).showErrorMessage(anyString());
    }

    @Test
    public void whenSendCommendFailed_thenShowError() {
        String exampleComment = "test";
        when(viewMock.getCommentText()).thenReturn(exampleComment);
        when(shotDetailsControllerMock
                .sendComment(anyLong(), eq(exampleComment)))
                .thenReturn(Single.error(new Throwable("test")));

        shotDetailsPresenter.sendComment();

        verify(viewMock, times(1)).showErrorMessage(anyString());
    }

    @Test
    public void whenShotDetailsDownloadFail_thenShowPreLoadedDetailsAndShowError() {
        String expectedMessage = "test";
        Throwable throwable = new Throwable(expectedMessage);
        when(errorMessageControllerMock.getErrorMessageLabel(throwable)).thenCallRealMethod();
        when(shotDetailsControllerMock.getShotComments(anyLong(), anyInt()))
                .thenReturn(Observable.error(throwable));

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(any(Shot.class));
        verify(viewMock, times(1)).initView();
        verify(viewMock, times(1)).showErrorMessage(anyString());
    }

    @Test
    public void whenShotLikingFailed_thenShowError() {
        String expectedMessage = "test";
        Throwable throwable = new Throwable(expectedMessage);
        when(errorMessageControllerMock.getErrorMessageLabel(throwable)).thenCallRealMethod();
        when(shotDetailsControllerMock.performLikeAction(anyLong(), anyBoolean()))
                .thenReturn(Completable.error(throwable));

        shotDetailsPresenter.handleShotLike(true);

        verify(viewMock, never()).showDetails(any(Shot.class));
        verify(viewMock, never()).initView();
        verify(viewMock, times(1)).showErrorMessage(anyString());
    }

    @After
    public void tearDown() {
        shotDetailsPresenter.detachView(false);
    }

}