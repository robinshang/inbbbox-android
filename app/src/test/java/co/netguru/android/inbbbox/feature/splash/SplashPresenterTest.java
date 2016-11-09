package co.netguru.android.inbbbox.feature.splash;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

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
    public ErrorMessageController errorMessageControllerMock;

    @Mock
    public SplashContract.View splashViewMock;

    @InjectMocks
    public SplashPresenter splashPresenter;

    @Test
    public void whenViewAttached_thenCheckTokenValidity() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER));

        splashPresenter.attachView(splashViewMock);

        verify(tokenControllerMock, times(1)).isTokenValid();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalid_thenGetUserInstance() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER));

        splashPresenter.attachView(splashViewMock);

        verify(userControllerMock, times(1)).requestUser();
    }

    @Test
    public void whenViewAttachedAndUserInstanceIsSaved_thenShowMainView() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.just(Statics.USER));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showMainScreen();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalidOrNull_thenShowLoginScreen() {
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(false));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowLoginScreen() {
        Throwable exampleThrowable = new Throwable();
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.error(exampleThrowable));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowErrorMessage() {
        String test = "test";
        Throwable exampleThrowable = new Throwable(test);
        when(tokenControllerMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userControllerMock.requestUser()).thenReturn(Observable.error(exampleThrowable));
        when(errorMessageControllerMock.getError(exampleThrowable)).thenReturn(test);

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showError(test);
    }
}