package co.netguru.android.inbbbox.feature.shots;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;

import static co.netguru.android.inbbbox.Statics.BUCKET;
import static co.netguru.android.inbbbox.Statics.LIKED_SHOT;
import static co.netguru.android.inbbbox.Statics.NOT_LIKED_SHOT;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotsController shotsControllerMock;

    @Mock
    ErrorMessageController errorMessageControllerMock;

    @Mock
    LikedShotsController likedShotsControllerMock;

    @Mock
    LikeShotController likeShotControllerMock;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    ShotsContract.View viewMock;

    @InjectMocks
    ShotsPresenter presenter;

    private final List<Shot> shotsList = new ArrayList<>();

    @Before
    public void setUp() {
        presenter.attachView(viewMock);

        int exampleId = 99;
        Shot exampleShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .creationDate(LocalDateTime.now())
                .build();
        shotsList.add(exampleShot);

        when(shotsControllerMock.getShots()).thenReturn(Observable.just(shotsList));
    }

    @Test
    public void whenDataLoadedCalled_thenRequestDataFromProvider() {

        presenter.loadData();

        verify(shotsControllerMock, times(1)).getShots();
    }

    @Test
    public void whenDataLoadedCorrectly_thenHideLoadingIndicator() {
        when(shotsControllerMock.getShots()).thenReturn(Observable.just(new ArrayList<>()));

        presenter.loadData();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadedCorrectly_thenShowDownloadedItems() {
        List<Shot> expectedShots = new ArrayList<>();
        when(shotsControllerMock.getShots()).thenReturn(Observable.just(expectedShots));

        presenter.loadData();

        verify(viewMock, times(1)).showItems(expectedShots);
    }

    @Test
    public void whenShotNotLikedAndLikeActionCalled_thenCallLikeShotMethod() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();

        presenter.likeShot(NOT_LIKED_SHOT);

        verify(likeShotControllerMock, times(1)).likeShot(NOT_LIKED_SHOT.id());
    }

    @Test
    public void whenShotLiked_thenChangeShotStatus() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();
        Shot expectedShot = Shot.builder()
                .id(NOT_LIKED_SHOT.id())
                .isLiked(true)
                .creationDate(NOT_LIKED_SHOT.creationDate())
                .build();

        presenter.likeShot(NOT_LIKED_SHOT);

        verify(viewMock, times(1)).changeShotLikeStatus(expectedShot);
    }

    @Test
    public void whenShotLikedAndLikeActionCalled_thenDoNotCallLikeShotMethod() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();

        presenter.likeShot(LIKED_SHOT);

        verify(likeShotControllerMock, never()).likeShot(LIKED_SHOT.id());
    }

    //ERRORS
    @Test
    public void whenDataLoadingFailed_thenHideLoadingIndicator() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(shotsControllerMock.getShots()).thenReturn(Observable.error(exampleException));

        presenter.loadData();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadingFailed_thenShowErrorMessage() {
        String message = "test";
        Throwable exampleException = new Exception(message);
        when(shotsControllerMock.getShots()).thenReturn(Observable.error(exampleException));
        when(errorMessageControllerMock.getError(exampleException)).thenCallRealMethod();

        presenter.loadData();

        verify(viewMock, times(1)).showError(message);
    }

    @Test
    public void whenShotLikeingFailed_thenShowApiError() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.error(exampleException));
        when(errorMessageControllerMock.getError(exampleException)).thenCallRealMethod();
        presenter.loadData();

        presenter.likeShot(NOT_LIKED_SHOT);

        verify(viewMock, times(1)).showError(message);
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
        verify(viewMock, only()).showError(anyString());
    }
}