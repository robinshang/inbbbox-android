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

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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
    ShotsContract.View viewMock;

    @InjectMocks
    ShotsPresenter presenter;

    private List<Shot> shotsList = new ArrayList<>();
    private int exampleId = 99;

    @Before
    public void setUp() {
        presenter.attachView(viewMock);

        int exampleId = 99;
        Shot exampleShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .isGif(false)
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
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Completable.complete());
        presenter.loadData();
        Shot expectedShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .isGif(false)
                .date(LocalDateTime.now())
                .likesCount(123)
                .bucketCount(123)
                .isBucketed(false)
                .build();

        presenter.likeShot(expectedShot);

        verify(likeShotControllerMock, times(1)).likeShot(exampleId);
    }

    @Test
    public void whenShotLiked_thenChangeShotStatus() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Completable.complete());
        presenter.loadData();
        Shot expectedShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .isGif(false)
                .build();

        presenter.likeShot(expectedShot);

        verify(viewMock, times(1)).changeShotLikeStatus(any(Shot.class));
    }

    public void whenShotLikedAndLikeActionCalled_thenCallLikeShotMethod() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Completable.complete());
        Shot expectedShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .isGif(false)
                .build();
        presenter.loadData();


        presenter.likeShot(expectedShot);

        verify(likeShotControllerMock, times(1)).likeShot(exampleId);
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
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Completable.error(exampleException));
        when(errorMessageControllerMock.getError(exampleException)).thenCallRealMethod();
        Shot expectedShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(false)
                .isGif(false)
                .build();
        presenter.loadData();

        presenter.likeShot(expectedShot);

        verify(viewMock).showError(message);
    }
}