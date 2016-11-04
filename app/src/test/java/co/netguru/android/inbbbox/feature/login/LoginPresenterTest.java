package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.feature.Statics;
import co.netguru.android.inbbbox.feature.authentication.OauthUrlProvider;
import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorType;
import co.netguru.android.inbbbox.models.Token;
import co.netguru.android.inbbbox.models.User;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    private OauthUrlProvider oauthUrlProviderMock;

    @Mock
    private ErrorMessageParser errorMessageParser;

    @Mock
    private TokenProvider tokenProviderMock;

    @Mock
    private LoginContract.View viewMock;

    @Mock
    private Uri uri;

    @Mock
    private UserProvider userProviderMock;

    private String urlString = "www.google.com";
    private String code = "testCode";

    private Token expectedToken;

    @InjectMocks
    private LoginPresenter presenter;

    @Before
    public void setup() {
        expectedToken = new Token("", "", "");

        presenter.attachView(viewMock);
        when(tokenProviderMock.requestNewToken(code))
                .thenReturn(Observable.just(expectedToken));
        when(userProviderMock.requestUser()).
                thenReturn(Observable.just(Statics.USER));
    }

    @Test
    public void whenLoginClick_thenGetUrlFromUrlProviderTest() {
        when(oauthUrlProviderMock.getOauthAuthorizeUrlString()).thenReturn(Observable.just(urlString));

        presenter.showLoginView();

        verify(viewMock).handleOauthUrl(urlString);
    }

    @Test
    public void whenLoginClick_thenShowActionViewForOauthRequest() {
        when(oauthUrlProviderMock.getOauthAuthorizeUrlString()).thenReturn(Observable.just(urlString));

        presenter.showLoginView();

        verify(viewMock, times((1))).handleOauthUrl(urlString);
    }

    @Test
    public void whenRedirectUrlFromActionViewDetected_sendAuthorizationRequestWithReceivedCode() {
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(tokenProviderMock, times(1)).requestNewToken(code);
    }

    @Test
    public void whenTokenReceived_thenGetUser() {
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(userProviderMock, times(1)).requestUser();
    }

    @Test
    public void whenUserReceived_thenShowMainView() {
        String code = "testCode";
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock).showNextScreen();
    }

    @Test
    public void whenHandlingOauthResponse_thenCloseDialog() {
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(null);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock, times(1)).closeLoginDialog();
    }


    //Errors
    @Test
    public void whenHandlingOauthResponseWithoutCode_thenShowErrorFromMessageFromUri() {
        String errorMessage = "test error";
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(null);
        when(uri.getQueryParameter(Constants.OAUTH.ERROR_KEY)).thenReturn(errorMessage);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock).showApiError(errorMessage);
    }

    @Test
    public void whenHandlingOauthResponseWithoutCode_thenShowReceivedError() {
        String errorMessage = "test error";
        when(uri.getQueryParameter(Constants.OAUTH.ERROR_KEY)).thenReturn(errorMessage);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock).showApiError(errorMessage);
    }

    @Test
    public void whenHandlingOauthResponseWithoutCodeAndError_thenShownInvalidUriError() {
        String testError = "testError";
        when(errorMessageParser.getErrorLabel(ErrorType.INVALID_OAUTH_URL)).thenReturn(testError);
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(null);
        when(uri.getQueryParameter(Constants.OAUTH.ERROR_KEY)).thenReturn(null);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock, times(1)).showApiError(testError);
    }

    @Test
    public void whenGettingErrorWhenTokenSaving_thenRunThroawbleMessageHanding() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);
        when(tokenProviderMock.requestNewToken(code)).thenReturn(Observable.error(testThrowable));

        presenter.handleOauthLoginResponse(uri);

        verify(errorMessageParser, times(1)).getError(testThrowable);
    }

    @Test
    public void whenGettingErrorWhenTokenSaving_thenShowErrorMessageFromThrowable() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);
        when(tokenProviderMock.requestNewToken(code)).thenReturn(Observable.error(testThrowable));
        when(errorMessageParser.getError(testThrowable)).thenReturn(throwableText);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock, times(1)).showApiError(throwableText);
    }

    @Test
    public void whenGettingErrorWhenUserSaving_thenRunThroawbleMessageHanding() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);
        when(userProviderMock.requestUser()).thenReturn(Observable.error(testThrowable));

        presenter.handleOauthLoginResponse(uri);

        verify(errorMessageParser, times(1)).getError(testThrowable);
    }

    @Test
    public void whenGettingErrorWhenUserSaving_thenShowErrorMessageFromThrowable() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);
        when(userProviderMock.requestUser()).thenReturn(Observable.error(testThrowable));
        when(errorMessageParser.getError(testThrowable)).thenReturn(throwableText);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock).showApiError(throwableText);
    }

}
