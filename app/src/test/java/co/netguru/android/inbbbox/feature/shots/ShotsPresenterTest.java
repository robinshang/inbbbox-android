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

import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.likes.LikedShotsProvider;
import co.netguru.android.inbbbox.feature.shots.like.LikeResponseMapper;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotsProvider shotsProviderMock;

    @Mock
    ErrorMessageParser errorMessageParserMock;

    @Mock
    LikedShotsProvider likedShotsProviderMock;

    @Mock
    LikeResponseMapper likeResponseMapperMock;

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

        LikedShot likedShot = new LikedShot(99, "test");
        when(shotsProviderMock.getShots()).thenReturn(Observable.just(shotsList));
    }

    @Test
    public void whenDataLoadedCalled_thenRequestDataFromProvider() {

        presenter.loadData();

        verify(shotsProviderMock, times(1)).getShots();
    }

    @Test
    public void whenDataLoadedCorrectly_thenHideLoadingIndicator() {
        when(shotsProviderMock.getShots()).thenReturn(Observable.just(new ArrayList<>()));

        presenter.loadData();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadedCorrectly_thenShowDownloadedItems() {
        List<Shot> expectedShots = new ArrayList<>();
        when(shotsProviderMock.getShots()).thenReturn(Observable.just(expectedShots));

        presenter.loadData();

        verify(viewMock, times(1)).showItems(expectedShots);
    }

    @Test
    public void whenShotNotLikedAndLikeActionCalled_thenCallLikeShotMethod() {
        when(likeResponseMapperMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();

        presenter.likeShot(0);

        verify(likeResponseMapperMock, times(1)).likeShot(exampleId);
    }

    @Test
    public void whenShotLiked_thenChangeShotStatus() {
        when(likeResponseMapperMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();
        Shot expectedShot = Shot.builder()
                .id(exampleId)
                .title("test")
                .isLiked(true)
                .build();

        presenter.likeShot(0);

        verify(viewMock, times(1)).changeShotLikeStatus(expectedShot);
    }

    @Test
    public void whenShotLikedAndLikeActionCalled_thenCallLikeShotMethod() {
        when(likeResponseMapperMock.likeShot(anyInt())).thenReturn(Observable.empty());
        presenter.loadData();

        presenter.likeShot(0);

        verify(likeResponseMapperMock, times(1)).likeShot(exampleId);
    }

    //ERRORS
    @Test
    public void whenDataLoadingFailed_thenHideLoadingIndicator() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(shotsProviderMock.getShots()).thenReturn(Observable.error(exampleException));

        presenter.loadData();

        verify(viewMock, times(1)).hideLoadingIndicator();
    }

    @Test
    public void whenDataLoadingFailed_thenShowErrorMessage() {
        String message = "test";
        Throwable exampleException = new Exception(message);
        when(shotsProviderMock.getShots()).thenReturn(Observable.error(exampleException));
        when(errorMessageParserMock.getError(exampleException)).thenCallRealMethod();

        presenter.loadData();

        verify(viewMock, times(1)).showError(message);
    }

    @Test
    public void whenShotLikeingFailed_thenShowApiError() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(likeResponseMapperMock.likeShot(anyInt())).thenReturn(Observable.error(exampleException));
        when(errorMessageParserMock.getError(exampleException)).thenCallRealMethod();
        presenter.loadData();

        presenter.likeShot(0);

        verify(viewMock, times(1)).showError(message);
    }
}