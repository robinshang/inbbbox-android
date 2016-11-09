package co.netguru.android.inbbbox.feature.shots;

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

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsPresenterTest {

    private static final Shot MOCK_NOT_LIKED_SHOT = Shot.builder().id(1).isLiked(false).build();
    private static final Shot MOCK_LIKED_SHOT = Shot.builder().id(1).isLiked(true).build();

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

        presenter.likeShot(MOCK_NOT_LIKED_SHOT);

        verify(likeShotControllerMock, times(1)).likeShot(MOCK_NOT_LIKED_SHOT.id());
    }

    @Test
    public void whenShotLiked_thenChangeShotStatus() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();
        Shot expectedShot = Shot.builder()
                .id(MOCK_NOT_LIKED_SHOT.id())
                .isLiked(true)
                .build();

        presenter.likeShot(MOCK_NOT_LIKED_SHOT);

        verify(viewMock, times(1)).changeShotLikeStatus(expectedShot);
    }

    @Test
    public void whenShotLikedAndLikeActionCalled_thenDoNotCallLikeShotMethod() {
        when(likeShotControllerMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();

        presenter.likeShot(MOCK_LIKED_SHOT);

        verify(likeShotControllerMock, never()).likeShot(MOCK_LIKED_SHOT.id());
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

        presenter.likeShot(MOCK_NOT_LIKED_SHOT);

        verify(viewMock, times(1)).showError(message);
    }
}