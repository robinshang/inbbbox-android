package co.netguru.android.inbbbox.feature.details.fullscreen;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.details.ShotDetailsType;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class ShotFullscreenPresenterTest {

    private static final int SHOT_PAGE_COUNT = 15;
    private static final int SHOT_PAGE_2 = 2;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @InjectMocks
    ShotFullScreenPresenter shotFullScreenPresenter;

    @Mock
    ShotFullscreenContract.View viewMock;

    @Mock
    Shot shotMock;

    @Mock
    ShotsController shotsControllerMock;

    ShotDetailsRequest defaultDetailsRequest = ShotDetailsRequest.create(ShotDetailsType.DEFAULT);

    private List<Shot> shotsFullPage;

    private List<Shot> shotsHalfPage;

    @Before
    public void setUp() {
        shotFullScreenPresenter.attachView(viewMock);

        shotsFullPage = new ArrayList<>();
        for (int i = 0; i < SHOT_PAGE_COUNT; i++) {
            shotsFullPage.add(shotMock);
        }

        shotsHalfPage = new ArrayList<>();
        for (int i = 0; i < SHOT_PAGE_COUNT / 2; i++) {
            shotsHalfPage.add(shotMock);
        }
    }

    @Test
    public void whenViewCreatedWithSingleShot_thenPreviewSingleShot() {
        shotFullScreenPresenter.onViewCreated(shotMock, Collections.emptyList(), defaultDetailsRequest);

        verify(viewMock, times(1)).previewSingleShot(shotMock);
    }

    @Test
    public void whenViewCreatedWithSingleShot_thenPreviewShots() {
        List<Shot> shotList = Arrays.asList(shotMock);

        shotFullScreenPresenter.onViewCreated(shotMock, shotList, defaultDetailsRequest);

        verify(viewMock, times(1)).previewShots(shotMock, shotList);
    }

    @Test
    public void whenInitialShotsWereFullPageAndRequestedMore_thenShowMoreItems() {
        List<Shot> expectedShots = new ArrayList<>();
        when(shotsControllerMock.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        shotFullScreenPresenter.onViewCreated(shotMock, shotsFullPage, defaultDetailsRequest);
        shotFullScreenPresenter.onRequestMoreData();

        verify(viewMock, times(1)).showMoreItems(expectedShots);
    }

    @Test
    public void whenInitialShotsWereNotFullPageAndRequestedMore_thenDoNotShowMoreItems() {
        List<Shot> expectedShots = new ArrayList<>();
        when(shotsControllerMock.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        shotFullScreenPresenter.onViewCreated(shotMock, shotsHalfPage, defaultDetailsRequest);
        shotFullScreenPresenter.onRequestMoreData();

        verify(viewMock, never()).showMoreItems(expectedShots);
    }

    @Test
    public void whenBackArrowPressed_thenCloseView() {
        shotFullScreenPresenter.onBackArrowPressed();

        verify(viewMock, times(1)).close();
    }
}
