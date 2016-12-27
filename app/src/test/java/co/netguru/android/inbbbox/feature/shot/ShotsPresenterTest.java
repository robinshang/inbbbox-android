package co.netguru.android.inbbbox.feature.shot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.data.bucket.BucketsController;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;

import static co.netguru.android.inbbbox.Statics.BUCKET;
import static co.netguru.android.inbbbox.Statics.LIKED_SHOT;
import static co.netguru.android.inbbbox.Statics.NOT_LIKED_SHOT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsPresenterTest {

    private static final int SHOT_PAGE_COUNT = 15;
    private static final int SHOT_PAGE = 1;
    private static final Long EXAMPLE_ID = 9L;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotsController shotsControllerMock;

    @Mock
    ErrorController errorControllerMock;

    @Mock
    LikeShotControllerApi likeShotControllerApiMock;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    ShotsContract.View viewMock;

    @Mock
    Shot shotMock;

    @InjectMocks
    ShotsPresenter presenter;

    private final List<Shot> shotsList = new ArrayList<>();

    @Before
    public void setUp() {
        presenter.attachView(viewMock);

        Shot exampleShot = Statics.LIKED_SHOT;
        shotsList.add(exampleShot);

        when(shotMock.id()).thenReturn(EXAMPLE_ID);
        when(shotsControllerMock.getShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.just(shotsList));
    }

    @Test
    public void whenDataLoadedCalled_thenRequestDataFromProvider() {

        presenter.getShotsFromServer();

        verify(shotsControllerMock, times(1)).getShots(SHOT_PAGE, SHOT_PAGE_COUNT);
    }

    @Test
    public void whenDataLoadedCorrectly_thenHideLoadingIndicator() {
        when(shotsControllerMock.getShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.just(new ArrayList<>()));

        presenter.getShotsFromServer();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadedCorrectly_thenShowDownloadedItems() {
        List<Shot> expectedShots = new ArrayList<>();
        when(shotsControllerMock.getShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        presenter.getShotsFromServer();

        verify(viewMock, times(1)).setData(expectedShots);
    }

    @Test
    public void whenShotNotLikedAndLikeActionCalled_thenCallLikeShotMethod() {
        when(likeShotControllerApiMock.likeShot(NOT_LIKED_SHOT)).thenReturn(Completable.complete());
        presenter.getShotsFromServer();

        presenter.likeShot(NOT_LIKED_SHOT);

        verify(likeShotControllerApiMock, times(1)).likeShot(NOT_LIKED_SHOT);
    }

    @Test
    public void whenShotLiked_thenChangeShotStatus() {
        when(likeShotControllerApiMock.likeShot(any(Shot.class))).thenReturn(Completable.complete());
        presenter.getShotsFromServer();
        Shot expectedShot = Shot.update(Statics.LIKED_SHOT)
                .id(NOT_LIKED_SHOT.id())
                .isLiked(true)
                .creationDate(NOT_LIKED_SHOT.creationDate())
                .isGif(false)
                .build();

        presenter.likeShot(NOT_LIKED_SHOT);

        presenter.likeShot(expectedShot);

        verify(viewMock, times(1)).changeShotLikeStatus(any(Shot.class));
    }

    @Test
    public void whenShotLikedAndLikeActionCalled_thenDoNotCallLikeShotMethod() {
        when(likeShotControllerApiMock.likeShot(any(Shot.class))).thenReturn(Completable.complete());

        presenter.getShotsFromServer();

        presenter.likeShot(LIKED_SHOT);

        verify(likeShotControllerApiMock, never()).likeShot(shotMock);
    }

    //ERRORS
    @Test
    public void whenDataLoadingFailed_thenHideLoadingIndicator() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(shotsControllerMock.getShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.error(exampleException));

        presenter.getShotsFromServer();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadingFailed_thenShowErrorMessage() {
        String message = "test";
        Throwable exampleException = new Exception(message);
        when(shotsControllerMock.getShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.error(exampleException));
        when(errorControllerMock.getThrowableMessage(exampleException)).thenCallRealMethod();

        presenter.getShotsFromServer();

        verify(viewMock, times(1)).showMessageOnServerError(message);
    }

    @Test
    public void whenShotLikeingFailed_thenShowApiError() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(likeShotControllerApiMock.likeShot(any(Shot.class))).thenReturn(Completable.error(exampleException));
        when(errorControllerMock.getThrowableMessage(exampleException)).thenCallRealMethod();

        presenter.getShotsFromServer();

        presenter.likeShot(NOT_LIKED_SHOT);

        verify(viewMock).showMessageOnServerError(message);
    }

    @Test
    public void whenShotChosenToBeAddedToBucket_thenShowBucketChooser() {
        //when
        presenter.handleAddShotToBucket(LIKED_SHOT);
        //then
        verify(viewMock, times(1)).showBucketChoosing(LIKED_SHOT);
    }

    @Test
    public void whenBucketForShotChosen_thenAddToBucket() {
        //given
        when(bucketsControllerMock.addShotToBucket(BUCKET.id(), LIKED_SHOT.id()))
                .thenReturn(Completable.complete());
        //when
        presenter.addShotToBucket(BUCKET, LIKED_SHOT);
        //then
        verify(viewMock, only()).showBucketAddSuccess();
    }

    @Test
    public void whenBucketForShotChosenAndErrorOccurs_thenShowApiError() {
        //given
        when(bucketsControllerMock.addShotToBucket(BUCKET.id(), LIKED_SHOT.id()))
                .thenReturn(Completable.error(new Throwable()));
        //when
        presenter.addShotToBucket(BUCKET, LIKED_SHOT);
        //then
        verify(viewMock, times(1)).showMessageOnServerError(anyString());
    }
}