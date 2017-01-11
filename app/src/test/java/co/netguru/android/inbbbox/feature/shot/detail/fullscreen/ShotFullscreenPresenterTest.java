package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
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
    private static final int EXAMPLE_PREVIEW_SHOT_INDEX = 0;

    @Rule
    public TestRule rule = new RxSyncTestRule();

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

    List<Shot> allShots;

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

        allShots = new ArrayList<>();
        allShots.add(shotMock);
    }

    @Test
    public void whenViewCreated_thenPreviewShots() {
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, allShots, EXAMPLE_PREVIEW_SHOT_INDEX, defaultRequest);

        presenter.attachView(viewMock);

        verify(viewMock, times(1)).previewShots(allShots, EXAMPLE_PREVIEW_SHOT_INDEX);
    }

    @Test
    public void whenInitialShotsWereFullPageAndRequestedMore_thenShowMoreItems() {
        ShotFullScreenPresenter fullPagePresenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotsFullPage, EXAMPLE_PREVIEW_SHOT_INDEX, defaultRequest);
        fullPagePresenter.attachView(viewMock);
        when(shotsController.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        fullPagePresenter.onRequestMoreData();

        verify(viewMock, times(1)).showMoreItems(expectedShots);
    }

    @Test
    public void whenInitialShotsWereNotFullPageAndRequestedMore_thenDoNotShowMoreItems() {
        ShotFullScreenPresenter halfPagePresenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, shotsHalfPage, EXAMPLE_PREVIEW_SHOT_INDEX, defaultRequest);
        halfPagePresenter.attachView(viewMock);
        when(shotsController.getShots(SHOT_PAGE_2, SHOT_PAGE_COUNT)).thenReturn(Observable.just(expectedShots));

        halfPagePresenter.onRequestMoreData();

        verify(viewMock, never()).showMoreItems(expectedShots);
    }

    @Test
    public void whenBackArrowPressed_thenCloseView() {
        ShotFullScreenPresenter presenter = new ShotFullScreenPresenter(shotsController, likeShotController,
                bucketsController, userShotsController, allShots, EXAMPLE_PREVIEW_SHOT_INDEX, defaultRequest);

        presenter.attachView(viewMock);
        presenter.onBackArrowPressed();

        verify(viewMock, times(1)).close();
    }
}
