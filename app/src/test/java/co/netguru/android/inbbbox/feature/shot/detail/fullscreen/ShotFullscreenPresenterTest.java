package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

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

import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
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
    ShotsController shotsController;

    @Mock
    LikeShotController likeShotController;

    @Mock
    BucketsController bucketsController;

    @Mock
    UserShotsController userShotsController;

    List<Shot> shotsFullPage;

    List<Shot> shotsHalfPage;

    List<Shot> expectedShots = Collections.emptyList();

    ShotDetailsRequest defaultRequest = ShotDetailsRequest.builder()
            .detailsType(ShotDetailsType.DEFAULT)
            .build();

    @Before
    public void setUp() {
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
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotMock, Collections.emptyList(), defaultRequest);

        presenter.attachView(viewMock);

        verify(viewMock, times(1)).previewSingleShot(shotMock);
    }

    @Test
    public void whenViewCreatedWithSingleShot_thenPreviewShots() {
        List<Shot> shotList = Arrays.asList(shotMock);
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotMock, shotList, defaultRequest);

        presenter.attachView(viewMock);

        verify(viewMock, times(1)).previewShots(shotMock, shotList);
    }

    @Test
    public void whenInitialShotsWereFullPageAndRequestedMore_thenShowMoreItems() {
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotMock, shotsFullPage, defaultRequest);
        presenter.attachView(viewMock);
        when(shotsController.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        presenter.onRequestMoreData();

        verify(viewMock, times(1)).showMoreItems(expectedShots);
    }

    @Test
    public void whenInitialShotsWereNotFullPageAndRequestedMore_thenDoNotShowMoreItems() {
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotMock, shotsHalfPage, defaultRequest);
        presenter.attachView(viewMock);
        when(shotsController.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        presenter.onRequestMoreData();

        verify(viewMock, never()).showMoreItems(expectedShots);
    }

    @Test
    public void whenBackArrowPressed_thenCloseView() {
        shotFullScreenPresenter.attachView(viewMock);

        shotFullScreenPresenter.onBackArrowPressed();

        verify(viewMock, times(1)).close();
    }
}
