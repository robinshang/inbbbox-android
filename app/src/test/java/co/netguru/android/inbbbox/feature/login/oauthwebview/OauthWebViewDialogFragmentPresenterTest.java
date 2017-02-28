package co.netguru.android.inbbbox.feature.login.oauthwebview;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.session.model.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OauthWebViewDialogFragmentPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    OauthWebViewDialogFragmentContract.View viewMock;

    @Mock
    private Uri uriMock;

    @Mock
    TokenController tokenControllerMock;

    @Mock
    ErrorController errorControllerMock;

    @Mock
    UserController userControllerMock;

    @Mock
    AnalyticsEventLogger eventLoggerMock;

    @InjectMocks
    OauthWebViewDialogFragmentPresenter presenter;

    private Token expectedToken;

    private static final String CODE = "testCode";
    private static final String STATE_KEY_CODE = "someKey";

    @Before
    public void setup() {
        expectedToken = new Token("", "", "");

        when(tokenControllerMock.requestNewToken(CODE))
                .thenReturn(Observable.just(expectedToken));
        when(userControllerMock.requestUser()).
                thenReturn(Observable.just(Statics.USER_ENTITY));
        when(userControllerMock.disableGuestMode())
                .thenReturn(Completable.complete());

        presenter.attachView(viewMock);
    }

    @Test
    public void whenHandleData_thenLoadUrl() {
        //given
        String url = "someUrl";
        //when
        presenter.handleData(url, STATE_KEY_CODE);
        //then
        verify(viewMock).loadUrl(url);
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
    public void whenProperRedirectReceived_thenFinnishWithCodeReturn() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        when(uriMock.getScheme()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
        when(uriMock.getQueryParameter(Constants.OAUTH.STATE_KEY)).thenReturn(STATE_KEY_CODE);
        when(uriMock.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(CODE);
        //when
        presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock).finishWithCodeReturn(CODE);
    }

    @Test
    public void whenRedirectProperButStateKeyNotMatching_thenFinnishWithWrongStateKey() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        when(uriMock.getScheme()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
        when(uriMock.getQueryParameter(Constants.OAUTH.STATE_KEY)).thenReturn("wrong code");
        when(uriMock.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(CODE);
        //when
        presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock).finishWithStateKeyNotMatchingError();
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
    public void whenRedirectContainError_thenFinnishWithErrorReturn() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        String someError = "error";
        when(uriMock.getScheme()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
        when(uriMock.getQueryParameter(Constants.OAUTH.ERROR_KEY)).thenReturn(someError);
        //when
        presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock).finishWithError(someError);
    }

    @Test
    public void whenRedirectProperButCodeIsEmpty_thenFinnishWithUnknownError() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        when(uriMock.getScheme()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
        when(uriMock.getQueryParameter(Constants.OAUTH.STATE_KEY)).thenReturn(STATE_KEY_CODE);
        when(uriMock.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(null);
        //when
        presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock).finishWithUnknownError();
    }

    @Test
    public void whenUriIsNotRedirect_thenJustReturnFalse() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        when(uriMock.getScheme()).thenReturn("wrong redirect");
        //when
        boolean result = presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock, never()).finishWithUnknownError();
        verify(viewMock, never()).finishWithError(anyString());
        verify(viewMock, never()).finishWithStateKeyNotMatchingError();
        verify(viewMock, never()).finishWithCodeReturn(CODE);
        Assert.assertFalse(result);
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
}
