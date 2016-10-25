package co.netguru.android.inbbbox.feature.splash;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
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
    public TokenProvider tokenProviderMock;

    @Mock
    public UserProvider userProviderMock;

    @Mock
    public ErrorMessageParser errorMessageParserMock;

    @Mock
    public SplashContract.View splashViewMock;

    @InjectMocks
    public SplashPresenter splashPresenter;

    @Test
    public void whenViewAttached_thenCheckTokenValidity() {
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.just(true));

        splashPresenter.attachView(splashViewMock);

        verify(tokenProviderMock, times(1)).isTokenValid();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalid_thenGetUserInstance() {
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.just(true));

        splashPresenter.attachView(splashViewMock);

        verify(userProviderMock, times(1)).getUser();
    }

    @Test
    public void whenViewAttachedAndUserInstanceIsSaved_thenShowMainView() {
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.just(true));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showMainScreen();
    }

    @Test
    public void whenViewAttachedAndTokenIsInvalidOrNull_thenShowLoginScreen() {
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(false));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowLoginScreen() {
        Throwable exampleThrowable = new Throwable();
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.error(exampleThrowable));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceNotSaved_thenShowLoginScreen() {
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.just(false));

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showLoginScreen();
    }

    @Test
    public void whenViewAttachedAndUserInstanceErrorOccurred_thenShowErrorMessage() {
        String test = "test";
        Throwable exampleThrowable = new Throwable(test);
        when(tokenProviderMock.isTokenValid()).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).thenReturn(Observable.error(exampleThrowable));
        when(errorMessageParserMock.getError(exampleThrowable)).thenReturn(test);

        splashPresenter.attachView(splashViewMock);

        verify(splashViewMock, times(1)).showError(test);
    }
}