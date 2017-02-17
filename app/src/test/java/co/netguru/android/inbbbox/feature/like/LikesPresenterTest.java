package co.netguru.android.inbbbox.feature.like;

import org.junit.After;
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
import java.util.Random;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LikesPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public ErrorController errorControllerMock;

    @Mock
    public LikeShotController likeShotControllerMock;

    @Mock
    public RxBus rxBusMock;

    @Mock
    public ShotUpdatedEvent shotUpdatedEventMock;

    @Mock
    public LikesViewContract.View viewMock;

    @Mock
    public Shot shotMock;

    @InjectMocks
    public LikesPresenter likesPresenter;

    private List<Shot> shotList;
    private Random random;

    @Before
    public void setup() {
        random = new Random();
        shotList = new ArrayList<>();
        for (int i = 1; i < random.nextInt(); i++) {
            shotList.add(shotMock);
        }
        when(likeShotControllerMock.getLikedShots(anyInt(), anyInt()))
                .thenReturn(Observable.just(shotList));
        when(rxBusMock.getEvents(any())).thenReturn(Observable.just(shotUpdatedEventMock));
        when(shotUpdatedEventMock.getShot()).thenReturn(Statics.NOT_LIKED_SHOT);
        when(errorControllerMock.getThrowableMessage(any(Throwable.class)))
                .thenReturn("test");

        likesPresenter.attachView(viewMock);
    }

    @Test
    public void whenGetLikesFromServerRequested_thenGetShotLikesForFirstPage() {

        likesPresenter.getLikesFromServer();

        verify(likeShotControllerMock).getLikedShots(eq(1), anyInt());
    }

    @Test
    public void whenGetLikesFromServerComplete_thenHideProgressIndicaotr() {

        likesPresenter.getLikesFromServer();

        verify(viewMock).hideProgressBar();
    }

    @Test
    public void whenGetLikesFromServerComplete_thenSetDataAndShowContent() {

        likesPresenter.getLikesFromServer();

        verify(viewMock).setData(shotList);
        verify(viewMock).showContent();
    }

    @Test
    public void whenGetLikesFromServerFail_thenShowError() {
        when(likeShotControllerMock.getLikedShots(anyInt(), anyInt()))
                .thenReturn(Observable.error(new Throwable()));

        likesPresenter.getLikesFromServer();

        verify(viewMock).showMessageOnServerError(anyString());
    }

    @Test
    public void whenMoreLikedRequested_thenShowProgress() {

        likesPresenter.getMoreLikesFromServer();

        verify(viewMock).showLoadingMoreLikesView();
    }

    @Test
    public void whenMoreLikedRequested_thenGetShotsForNextPages() {

        likesPresenter.getMoreLikesFromServer();

        verify(likeShotControllerMock).getLikedShots(eq(2), anyInt());
    }

    @Test
    public void whenOpenDetailsClicked_thenShowDetailsScreen() {

        likesPresenter.showShotDetails(shotMock, shotList);

        verify(viewMock).openShowDetailsScreen(shotMock, shotList);
    }

    @Test
    public void whenDataIsEmpty_thenShowEmptyDataInfo() {

        likesPresenter.checkDataEmpty(true);

        verify(viewMock).showEmptyLikesInfo();
    }

    @Test
    public void whenDataIsNotEmpty_thenHideEmptyDataInfo() {

        likesPresenter.checkDataEmpty(false);

        verify(viewMock).hideEmptyLikesInfo();
    }

    @After
    public void tearDown() {
        likesPresenter.detachView(false);
    }
}