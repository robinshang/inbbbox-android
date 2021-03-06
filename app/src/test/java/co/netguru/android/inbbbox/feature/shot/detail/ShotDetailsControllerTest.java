package co.netguru.android.inbbbox.feature.shot.detail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerApi;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.api.CommentEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotDetailsControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotsApi shotApiMock;

    @Mock
    LikeShotControllerApi likeShotControllerApiMock;

    @Mock
    BucketsController bucketsController;

    @Mock
    ShotDetailsState state;

    @Mock
    UserController userControllerMock;

    @Mock
    User userMock;

    @Mock
    UserEntity userEntityMock;

    @Mock
    Shot shotMock;

    @InjectMocks
    ShotDetailsController controller;

    private List<CommentEntity> comments = Statics.generateCommentsEntity();
    private Long EXAMPLE_SHOT_ID = 1L;
    private Long EXAMPLE_USER_ID = 99L;
    private int examplePerPageValue = 10;


    @Before
    public void setUp() {
        when(shotApiMock.getShotComments(eq(EXAMPLE_SHOT_ID.toString()), anyInt(), anyInt()))
                .thenReturn(Observable.just(comments));
        when(userControllerMock.getUserFromCache()).thenReturn(Single.just(userMock));
        when(userMock.id()).thenReturn(EXAMPLE_USER_ID);
        when(userEntityMock.id()).thenReturn(EXAMPLE_USER_ID);
        when(shotMock.id()).thenReturn(EXAMPLE_SHOT_ID);
    }

    @Test
    public void whenShotSubscribed_thenReturnComments() {
        mockLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void whenShotSubscribedAndWasLiked_thenReturnShotLikeStateTrue() {
        mockLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(true, shotDetailsState.isLiked());
    }

    @Test
    public void whenShotSubscribedAndWasNotLiked_thenReturnShotLikeStateFalse() {
        mockNotLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(false, shotDetailsState.isLiked());
    }

    @Test
    public void whenShotSubscribedAndWasInBucket_thenReturnShotBucketStateTrue() {
        mockNotLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(true, shotDetailsState.isBucketed());
    }

    @Test
    public void whenShotSubscribedAndWasNotInBucket_thenReturnShotBucketStateFalse() {
        mockNotLikedShotState();
        mockNotInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(false, shotDetailsState.isBucketed());
    }

    @Test
    public void whenGetShotSubscribed_thenRunCommentsDownloadRequest() {
        mockNotLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotApiMock, atLeastOnce())
                .getShotComments(eq(EXAMPLE_SHOT_ID.toString()), anyInt(), anyInt());
    }

    @Test
    public void whenGetShotSubscribed_thenRunCommentsDownloadLikeStateRequest() {
        mockNotLikedShotState();
        mockInBucketShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(shotMock, examplePerPageValue).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerApiMock, times(1)).isShotLiked(shotMock);
    }

    @Test
    public void whenLikePerformSubscribed_thenSendLikeRequest() {
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerApiMock.likeShot(shotMock)).thenReturn(Completable.complete());

        controller.performLikeAction(shotMock, true).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerApiMock, times(1)).likeShot(shotMock);
    }

    @Test
    public void whenLikePerformSubscribed_thenSendUnlikeRequest() {
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerApiMock.unLikeShot(shotMock)).thenReturn(Completable.complete());

        controller.performLikeAction(shotMock, false).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerApiMock, times(1)).unLikeShot(shotMock);
    }

    @Test
    public void whenSendCommentSubscribed_thenReturnNewCommentWhenTaskIsComplete() {
        Long id = 99L;
        String exampleText = "test";
        CommentEntity exampleComment = Statics.generateCommentsEntity().get(0);
        TestSubscriber<Comment> testSubscriber = new TestSubscriber<>();
        when(userControllerMock.getUserFromCache()).thenReturn(Single.just(userMock));
        when(shotApiMock
                .createComment(anyString(), anyString()))
                .thenReturn(Single.just(exampleComment));

        controller.sendComment(id, exampleText).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotApiMock, times(1)).createComment(id.toString(), exampleText);
    }

    @Test
    public void whenCommentDeleteSubscribed_thenReturnCompleteWhenSuccess() {
        when(shotApiMock.deleteComment(anyString(), anyString())).thenReturn(Completable.complete());
        TestSubscriber testSubscriber = new TestSubscriber();

        controller.deleteComment(99L, 88L).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    //ERRORS
    @Test
    public void whenLikePerformSubscribedAndErrorOccurredWhileLike_thenReturnError() {
        Throwable throwable = new Throwable("test");
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerApiMock.likeShot(shotMock))
                .thenReturn(Completable.error(throwable));

        controller.performLikeAction(shotMock, true).subscribe(testSubscriber);

        testSubscriber.assertError(throwable);
    }

    @Test
    public void whenLikePerformSubscribedAndErrorOccurredWhileUnlike_thenReturnError() {
        Throwable throwable = new Throwable("test");
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerApiMock.likeShot(shotMock))
                .thenReturn(Completable.error(throwable));
        when(likeShotControllerApiMock.unLikeShot(shotMock))
                .thenReturn(Completable.error(throwable));


        controller.performLikeAction(shotMock, false).subscribe(testSubscriber);

        testSubscriber.assertError(throwable);
    }

    private void mockLikedShotState() {
        when(likeShotControllerApiMock.isShotLiked(shotMock))
                .thenReturn(Single.just(true));
    }

    private void mockNotLikedShotState() {
        when(likeShotControllerApiMock.isShotLiked(shotMock))
                .thenReturn(Single.just(false));
    }

    private void mockInBucketShotState() {
        when(bucketsController.isShotBucketed(anyInt())).thenReturn(Single.just(Boolean.TRUE));
    }

    private void mockNotInBucketShotState() {
        when(bucketsController.isShotBucketed(anyInt()))
                .thenReturn(Single.just(Boolean.FALSE));
    }
}