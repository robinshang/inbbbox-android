package co.netguru.android.inbbbox.feature.peekandpop;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.other.VibrationController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPopPresenter;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotPeekAndPopPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotPeekAndPop viewMock;

    @InjectMocks
    ShotPeekAndPopPresenter presenter;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    ErrorController errorControllerMock;

    @Mock
    LikeShotController likeShotControllerMock;

    @Mock
    RxBus rxBusMock;

    @Mock
    Shot shotMock;

    @Mock
    VibrationController vibrationControllerMock;

    Shot likedShot = Statics.LIKED_SHOT_NOT_BUCKETED;
    Shot unlikedShot = Statics.NOT_LIKED_SHOT;

    Bucket exampleBucket = Statics.BUCKET;
    Exception exampleException = new Exception("Exception");

    @Before
    public void setup() {
        presenter.attachView(viewMock);
        when(errorControllerMock.getThrowableMessage(exampleException))
                .thenReturn(exampleException.getMessage());

        when(likeShotControllerMock.likeShot(any(Shot.class)))
                .thenReturn(Completable.complete());

        when(likeShotControllerMock.unLikeShot(any(Shot.class)))
                .thenReturn(Completable.complete());

        when(bucketsControllerMock.addShotToBucket(anyLong(), any(Shot.class)))
                .thenReturn(Completable.complete());
    }

    @Test
    public void whenAddShotToBucket_thenAddToBucket() {
        presenter.addShotToBucket(likedShot, exampleBucket);

        verify(bucketsControllerMock).addShotToBucket(exampleBucket.id(), likedShot);
    }

    @Test
    public void whenShotAddedToBucket_thenShowSuccess() {
        presenter.addShotToBucket(likedShot, exampleBucket);

        verify(viewMock).showBucketAddSuccess();
    }

    @Test
    public void whenAddShotToBucketFail_thenShowErrorMessage() {
        when(bucketsControllerMock.addShotToBucket(exampleBucket.id(), likedShot))
                .thenReturn(Completable.error(exampleException));

        presenter.addShotToBucket(likedShot, exampleBucket);

        verify(viewMock).showMessageOnServerError(exampleException.getMessage());
    }

    @Test
    public void whenShotAddedToBucket_thenSendBusEvent() {
        presenter.addShotToBucket(likedShot, exampleBucket);

        verify(rxBusMock).send(any(ShotUpdatedEvent.class));
    }

    @Test
    public void whenToggleLikeShotAndShotIsLiked_thenUnlikeShot() {
        createPresenterWithLikedShot();

        presenter.toggleLikeShot();

        verify(likeShotControllerMock).unLikeShot(likedShot);
        verify(likeShotControllerMock, times(0)).likeShot(likedShot);
    }

    @Test
    public void whenToggleLikeShotAndShotIsNotLiked_thenLikeShot() {
        createPresenterWithUnlikedShot();

        presenter.toggleLikeShot();

        verify(likeShotControllerMock).likeShot(unlikedShot);
        verify(likeShotControllerMock, times(0)).unLikeShot(likedShot);
    }

    @Test
    public void whenLikeShot_thenShowMessage() {
        createPresenterWithUnlikedShot();

        presenter.toggleLikeShot();

        verify(viewMock).showMessageShotLiked();
    }

    @Test
    public void whenUnlikeShot_thenShowMessage() {
        createPresenterWithLikedShot();

        presenter.toggleLikeShot();

        verify(viewMock).showMessageShotUnliked();
    }

    @Test
    public void whenLikeFailed_thenShowErrorMessage() {
        createPresenterWithUnlikedShot();
        when(likeShotControllerMock.likeShot(any(Shot.class)))
                .thenReturn(Completable.error(exampleException));

        presenter.toggleLikeShot();

        verify(viewMock).showMessageOnServerError(exampleException.getMessage());
    }

    @Test
    public void whenUnlikeFailed_thenShowErrorMessage() {
        createPresenterWithLikedShot();
        when(likeShotControllerMock.unLikeShot(any(Shot.class)))
                .thenReturn(Completable.error(exampleException));

        presenter.toggleLikeShot();

        verify(viewMock).showMessageOnServerError(exampleException.getMessage());
    }

    @Test
    public void whenShotLikedSuccess_thenSendBusEvent() {
        createPresenterWithUnlikedShot();

        presenter.toggleLikeShot();

        verify(rxBusMock).send(any(ShotUpdatedEvent.class));
    }

    @Test
    public void whenShotUnlikedSuccess_thenSendBusEvent() {
        createPresenterWithLikedShot();

        presenter.toggleLikeShot();

        verify(rxBusMock).send(any(ShotUpdatedEvent.class));
    }

    @Test
    public void whenBucketShot_thenShowChooserView() {
        presenter.onBucketShot();

        verify(viewMock).showBucketChooserView(shotMock);
    }

    private void createPresenterWithUnlikedShot() {
        presenter = new ShotPeekAndPopPresenter(likeShotControllerMock, errorControllerMock,
                rxBusMock, bucketsControllerMock, unlikedShot, vibrationControllerMock);
        presenter.attachView(viewMock);
    }

    private void createPresenterWithLikedShot() {
        presenter = new ShotPeekAndPopPresenter(likeShotControllerMock, errorControllerMock,
                rxBusMock, bucketsControllerMock, likedShot, vibrationControllerMock);
        presenter.attachView(viewMock);
    }

}