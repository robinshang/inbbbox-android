package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;
import android.support.v4.util.Pair;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import co.netguru.android.inbbbox.data.session.model.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    private TokenParametersController tokenParametersControllerMock;

    @Mock
    private ErrorController errorControllerMock;

    @Mock
    private TokenController tokenControllerMock;

    @Mock
    private LoginContract.View viewMock;

    @Mock
    private Uri uri;

    @Mock
    private UserController userControllerMock;

    @Mock
    private AnalyticsEventLogger eventLoggerMock;

    private static final String URL_STRING = "www.google.com";
    private static final UUID UUID_STATIC = UUID.randomUUID();
    private static final String CODE = "testCode";

    private Token expectedToken;

    @InjectMocks
    private LoginPresenter presenter;

    @Before
    public void setup() {
        expectedToken = new Token("", "", "");

        presenter.attachView(viewMock);
        when(tokenControllerMock.requestNewToken(CODE))
                .thenReturn(Observable.just(expectedToken));
        when(userControllerMock.requestUser()).
                thenReturn(Observable.just(Statics.USER_ENTITY));
        when(userControllerMock.disableGuestMode())
                .thenReturn(Completable.complete());
    }

    @Test
    public void whenLoginClick_thenShowActionViewForOauthRequest() {
        when(tokenParametersControllerMock.getOauthAuthorizeUrlAndUuidPair())
                .thenReturn(Observable.just(Pair.create(URL_STRING, UUID_STATIC)));

        presenter.showLoginView();

        verify(tokenParametersControllerMock, times(1)).getOauthAuthorizeUrlAndUuidPair();
        verify(viewMock, times((1))).openAuthWebViewFragment(URL_STRING, UUID_STATIC.toString());
        verify(viewMock, times(1)).disableLoginButton();
    }


    @Test
    public void whenCodeReceivedThen_getTokenUserAndFinish() {

        presenter.handleOauthCodeReceived(CODE);

        verify(userControllerMock, times(1)).requestUser();
        verify(tokenControllerMock).requestNewToken(CODE);
        verify(viewMock).showNextScreen();
        verify(eventLoggerMock).logEventLoginSuccess();
    }

    @Test
    public void whenWebViewClose_thenEnableLoginButton() {
        presenter.handleWebViewClose();

        verify(viewMock, times(1)).enableLoginButton();
    }

    @Test
    public void whenKeyNotMatching_thenShowWrongKeyError() {

        presenter.handleKeysNotMatching();

        verify(viewMock, times(1)).showWrongKeyError();
    }

    @Test
    public void whenUnknownOauthError_thenShowInvalidOauthUrlError() {

        presenter.handleUnknownOauthError();

        verify(viewMock, times(1)).showInvalidOauthUrlError();
    }

    @Test
    public void whenKnownOauthError_thenShowErrorMessage() {
        String message = "test";

        presenter.handleKnownOauthError(message);

        verify(viewMock, times(1)).showMessageOnServerError(message);
    }

    @Test
    public void whenLogoClickedLessThanThresholdValue_thenDoNothing() {

        for (int i = 0; i < 4; i++) {
            presenter.checkGuestMode();
        }

        verify(viewMock, never()).showGuestModeLoginButton();
    }

    @Test
    public void whenLogoClickedEqualsThresholdValue_thenShowGuestMode() {

        for (int i = 0; i < 5; i++) {
            presenter.checkGuestMode();
        }

        verify(viewMock, times(1)).showGuestModeLoginButton();
    }

    @Test
    public void whenLogoClickedMoreThenThresholdValue_thenShowGuestMode() {

        for (int i = 0; i < 1000; i++) {
            presenter.checkGuestMode();
        }

        verify(viewMock, times(1)).showGuestModeLoginButton();
    }

    @Test
    public void whenLoginAsGuestClicked_thenGetDefaultPublicToken() {
        String testToken = "token";
        Token tokenMock = mock(Token.class);
        when(tokenMock.getAccessToken()).thenReturn(testToken);
        when(tokenParametersControllerMock.getUserGuestToken()).thenReturn(Single.just(tokenMock));
        when(userControllerMock.enableGuestMode()).thenReturn(Completable.complete());

        presenter.loginWithGuestClicked();

        verify(tokenParametersControllerMock, times(1)).getUserGuestToken();
    }

    @Test
    public void whenDefaultPublicTokenRetrieveSuccess_thenSaveToken() {
        String testToken = "token";
        Token tokenMock = mock(Token.class);
        when(tokenMock.getAccessToken()).thenReturn(testToken);
        when(tokenParametersControllerMock.getUserGuestToken()).thenReturn(Single.just(tokenMock));
        when(userControllerMock.enableGuestMode()).thenReturn(Completable.complete());

        presenter.loginWithGuestClicked();

        verify(tokenControllerMock, times(1)).saveToken(tokenMock);
    }

    @Test
    public void whenDefaultPublicTokenRetrieveSuccess_thenEnableGuestMode() {
        String testToken = "token";
        Token tokenMock = mock(Token.class);
        when(tokenMock.getAccessToken()).thenReturn(testToken);
        when(tokenParametersControllerMock.getUserGuestToken()).thenReturn(Single.just(tokenMock));
        when(userControllerMock.enableGuestMode()).thenReturn(Completable.complete());

        presenter.loginWithGuestClicked();

        verify(userControllerMock, times(1)).enableGuestMode();
    }

    @Test
    public void whenGuestModeEnabled_thenShowMainScreen() {
        String testToken = "token";
        Token tokenMock = mock(Token.class);
        when(tokenMock.getAccessToken()).thenReturn(testToken);
        when(tokenParametersControllerMock.getUserGuestToken()).thenReturn(Single.just(tokenMock));
        when(userControllerMock.enableGuestMode()).thenReturn(Completable.complete());
        when(tokenControllerMock.saveToken(any(Token.class))).thenReturn(Completable.complete());
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();

        presenter.loginWithGuestClicked();

        verify(viewMock, times(1)).showNextScreen();
        verify(eventLoggerMock).logEventLoginGuest();
    }

    @Test
    public void whenAnyErrorOccurDuringEnablingGuestMode_thenShowError() {
        String errorMessage = "error";
        String testToken = "token";
        Token tokenMock = mock(Token.class);
        when(tokenMock.getAccessToken()).thenReturn(testToken);
        when(tokenParametersControllerMock.getUserGuestToken()).thenReturn(Single.just(tokenMock));
        when(userControllerMock.enableGuestMode()).thenReturn(Completable.complete());
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();
        when(tokenControllerMock.saveToken(any(Token.class)))
                .thenReturn(Completable.error(new Throwable(errorMessage)));

        presenter.loginWithGuestClicked();

        verify(viewMock, times(1)).showMessageOnServerError(errorMessage);
        verify(eventLoggerMock).logEventLoginFail();
    }

    //Error
    @Test
    public void whenGettingErrorWhenTokenSaving_thenRunThrowableMessageHanding() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(tokenControllerMock.requestNewToken(CODE)).thenReturn(Observable.error(testThrowable));

        presenter.handleOauthCodeReceived(CODE);

        verify(viewMock, never()).showNextScreen();
        verify(viewMock).showMessageOnServerError(anyString());
        verify(errorControllerMock, times(1)).getThrowableMessage(testThrowable);
    }

    @After
    public void tearDown() {
        presenter.detachView(false);
    }

}
