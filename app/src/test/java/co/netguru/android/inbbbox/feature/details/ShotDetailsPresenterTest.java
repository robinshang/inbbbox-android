package co.netguru.android.inbbbox.feature.details;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotDetailsPresenterTest {

    private static final long EXAMPLE_ID = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Shot shotMock;

    @Mock
    ShotDetailsController shotDetailsControllerMock;

    @Mock
    ErrorMessageController errorMessageControllerMock;

    @Mock
    ShotDetailsContract.View viewMock;

    @InjectMocks
    ShotDetailsPresenter shotDetailsPresenter;

    @Before
    public void setUp() {
        shotDetailsPresenter.attachView(viewMock);
        when(shotMock.id()).thenReturn(EXAMPLE_ID);
        when(shotMock.author()).thenReturn(User.create(Statics.USER_ENTITY));
        when(shotMock.creationDate()).thenReturn(LocalDateTime.now());
        when(viewMock.getShotInitialData()).thenReturn(shotMock);
        shotDetailsPresenter.retrieveInitialData();
    }

    @Test
    public void whenShotDetailsDownload_thenShowMainShotImageWithShotImageInterface() {
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID)).thenReturn(Observable.empty());

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showMainImage(shotMock);
    }

    @Test
    public void whenShotDetailsDownload_thenShowDetailsWithShotData() {
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID)).thenReturn(Observable.empty());

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(shotMock);
        verify(viewMock, times(1)).initView();
    }

    @Test
    public void whenShotDetailsDownload_thenLoadDetailsAndUpdateItAfterStateAreDownloaded() {
        boolean expectedLikeState = true;
        ShotDetailsState resultState = ShotDetailsState.create(expectedLikeState, false, Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID)).thenReturn(Observable.just(resultState));
        when(shotMock.isLiked()).thenReturn(expectedLikeState);
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(2)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(expectedLikeState, shot.isLiked());
        verify(viewMock, times(2)).initView();
    }

    @Test
    public void whenShotDetailsDownload_thenShowShotNotLiked() {
        boolean expectedLikeState = false;
        ShotDetailsState resultState = ShotDetailsState.create(expectedLikeState, false, Statics.COMMENTS);
        when(shotDetailsControllerMock.getShotComments(EXAMPLE_ID)).thenReturn(Observable.just(resultState));
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(2)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(shot.isLiked(), expectedLikeState);
        verify(viewMock, times(2)).initView();
    }

    @Test
    public void whenShotLiked_thenPerformLikeActionWithTrue() {
        boolean likeState = true;
        when(shotDetailsControllerMock.performLikeAction(EXAMPLE_ID, likeState))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.handleShotLike(likeState);

        verify(shotDetailsControllerMock, times(1)).performLikeAction(EXAMPLE_ID, likeState);
    }

    @Test
    public void whenShotLiked_thenPerformLikeActionWithFalse() {
        boolean likeState = false;
        when(shotDetailsControllerMock.performLikeAction(EXAMPLE_ID, likeState))
                .thenReturn(Completable.complete());

        shotDetailsPresenter.handleShotLike(likeState);

        verify(shotDetailsControllerMock, times(1)).performLikeAction(EXAMPLE_ID, likeState);
    }

    @Test
    public void whenShotLiked_thenUpdateUserDetails() {
        boolean likeState = false;
        when(shotDetailsControllerMock.performLikeAction(anyLong(), anyBoolean()))
                .thenReturn(Completable.complete());
        ArgumentCaptor<Shot> argumentCaptor = ArgumentCaptor.forClass(Shot.class);

        shotDetailsPresenter.handleShotLike(likeState);

        verify(viewMock, times(1)).showDetails(argumentCaptor.capture());
        Shot shot = argumentCaptor.getValue();
        Assert.assertEquals(shot.isLiked(), likeState);
        verify(viewMock, times(1)).initView();
    }

    //ERRORS
    @Test
    public void whenShotDetailsDownloadFail_thenShowPreLoadedDetailsAndShowError() {
        String expectedMessage = "test";
        Throwable throwable = new Throwable(expectedMessage);
        when(errorMessageControllerMock.getErrorMessageLabel(throwable)).thenCallRealMethod();
        when(shotDetailsControllerMock.getShotComments(anyLong()))
                .thenReturn(Observable.error(throwable));

        shotDetailsPresenter.downloadData();

        verify(viewMock, times(1)).showDetails(any(Shot.class));
        verify(viewMock, times(1)).initView();
        verify(viewMock, times(1)).showErrorMessage(expectedMessage);
    }

    @Test
    public void whenShotLikingFailed_thenShowError() {
        String expectedMessage = "test";
        Throwable throwable = new Throwable(expectedMessage);
        when(errorMessageControllerMock.getErrorMessageLabel(throwable)).thenCallRealMethod();
        when(shotDetailsControllerMock.performLikeAction(anyLong(), anyBoolean()))
                .thenReturn(Completable.error(throwable));

        shotDetailsPresenter.handleShotLike(true);

        verify(viewMock, never()).showDetails(any(Shot.class));
        verify(viewMock, never()).initView();
        verify(viewMock, times(1)).showErrorMessage(expectedMessage);
    }

    @After
    public void tearDown() {
        shotDetailsPresenter.detachView(false);
    }

}