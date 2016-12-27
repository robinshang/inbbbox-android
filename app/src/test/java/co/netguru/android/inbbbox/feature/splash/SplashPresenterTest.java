package co.netguru.android.inbbbox.feature.splash;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public TokenController tokenControllerMock;

    @Mock
    public UserController userControllerMock;

    @Mock
    public ErrorController errorControllerMock;

    @Mock
    public SplashContract.View splashViewMock;

    @InjectMocks
    public SplashPresenter splashPresenter;

    @Test
    public void whenViewAttached_thenCheckTokenValidity() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER_ENTITY));

        splashPresenter.attachView(splashViewMock);

        verify(tokenControllerMock, times(1)).isTokenValid();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalid_thenGetUserInstance() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER_ENTITY));
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        splashPresenter.attachView(splashViewMock);

        verify(userControllerMock, times(1)).requestUser();
    }

    @Test
    public void whenViewAttachedAndUserInstanceIsSaved_thenShowMainView() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(true));
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER_ENTITY));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showMainScreen();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalidOrNull_thenShowLoginScreen() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(false));
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowLoginScreen() {
        Throwable exampleThrowable = new Throwable();
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.error(exampleThrowable));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowErrorMessage() {
        String test = "test";
        Throwable exampleThrowable = new Throwable(test);
        when(tokenControllerMock.isTokenValid()).thenReturn(Single.just(true));
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
        when(userControllerMock.requestUser()).thenReturn(Observable.error(exampleThrowable));
        when(errorControllerMock.getThrowableMessage(exampleThrowable)).thenReturn(test);

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showMessageOnServerError(test);
    }

    @After
    public void tearDown() {
        splashPresenter.detachView(false);
    }
}