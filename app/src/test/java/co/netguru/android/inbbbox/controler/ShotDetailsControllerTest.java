package co.netguru.android.inbbbox.controler;

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
import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.model.api.CommentEntity;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotDetailsControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotCommentsApi shotCommentsApiMock;

    @Mock
    LikeShotController likeShotControllerMock;

    @Mock
    ShotDetailsState state;

    @InjectMocks
    ShotDetailsController controller;

    private List<CommentEntity> comments = Statics.generateCommentsEntity();
    private Long EXAMPLE_SHOT_ID = 1L;

    @Before
    public void setUp() {
        when(shotCommentsApiMock.getComments(EXAMPLE_SHOT_ID.toString()))
                .thenReturn(Observable.just(comments));
    }

    @Test
    public void whenShotSubscribed_thenReturnComments() {
        mockLikedShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(EXAMPLE_SHOT_ID).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void whenShotSubscribedAndWasLiked_thenReturnShotLikeStateTrue() {
        mockLikedShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(EXAMPLE_SHOT_ID).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(true, shotDetailsState.isLiked());
    }

    @Test
    public void whenShotSubscribedAndWasNotLiked_thenReturnShotLikeStateFalse() {
        mockNotLikedShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(EXAMPLE_SHOT_ID).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        ShotDetailsState shotDetailsState = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(false, shotDetailsState.isLiked());
    }

    @Test
    public void whenGetShotSubscribed_thenRunCommentsDownloadRequest() {
        mockNotLikedShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(EXAMPLE_SHOT_ID).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotCommentsApiMock, times(1)).getComments(EXAMPLE_SHOT_ID.toString());
    }

    @Test
    public void whenGetShotSubscribed_thenRunCommentsDownloadLikeStateRequest() {
        mockNotLikedShotState();
        TestSubscriber<ShotDetailsState> testSubscriber = new TestSubscriber<>();

        controller.getShotComments(EXAMPLE_SHOT_ID).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerMock, times(1)).isShotLiked(EXAMPLE_SHOT_ID);
    }

    @Test
    public void whenLikePerformSubscribed_thenSendLikeRequest() {
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerMock.likeShot(EXAMPLE_SHOT_ID)).thenReturn(Completable.complete());

        controller.performLikeAction(EXAMPLE_SHOT_ID, true).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerMock, times(1)).likeShot(EXAMPLE_SHOT_ID);
    }

    @Test
    public void whenLikePerformSubscribed_thenSendUnlikeRequest() {
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerMock.unLikeShot(EXAMPLE_SHOT_ID)).thenReturn(Completable.complete());

        controller.performLikeAction(EXAMPLE_SHOT_ID, false).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likeShotControllerMock, times(1)).unLikeShot(EXAMPLE_SHOT_ID);
    }

    //ERRORS
    @Test
    public void whenLikePerformSubscribedAndErrorOccurredWhileLike_thenReturnError() {
        Throwable throwable = new Throwable("test");
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerMock.likeShot(EXAMPLE_SHOT_ID))
                .thenReturn(Completable.error(throwable));

        controller.performLikeAction(EXAMPLE_SHOT_ID, true).subscribe(testSubscriber);

        testSubscriber.assertError(throwable);
    }

    @Test
    public void whenLikePerformSubscribedAndErrorOccurredWhileUnlike_thenReturnError() {
        Throwable throwable = new Throwable("test");
        TestSubscriber testSubscriber = new TestSubscriber<>();
        when(likeShotControllerMock.likeShot(EXAMPLE_SHOT_ID))
                .thenReturn(Completable.error(throwable));
        when(likeShotControllerMock.unLikeShot(EXAMPLE_SHOT_ID))
                .thenReturn(Completable.error(throwable));


        controller.performLikeAction(EXAMPLE_SHOT_ID, false).subscribe(testSubscriber);

        testSubscriber.assertError(throwable);
    }

    private void mockLikedShotState() {
        when(likeShotControllerMock.isShotLiked(EXAMPLE_SHOT_ID))
                .thenReturn(Completable.complete());
    }

    private void mockNotLikedShotState() {
        when(likeShotControllerMock.isShotLiked(EXAMPLE_SHOT_ID))
                .thenReturn(Completable.error(new Throwable()));
    }
}