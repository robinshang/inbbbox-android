package co.netguru.android.inbbbox.login.oauthwebview;

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
import co.netguru.android.testcommons.RxSyncTestRule;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
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

    @InjectMocks
    OauthWebViewDialogFragmentPresenter presenter;

    private static final String CODE = "testCode";
    private static final String STATE_KEY_CODE = "someKey";

    @Before
    public void setup() {
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
    public void whenProperRedirectReceived_thenFinnishWithCodeReturn() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        when(uriMock.toString()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
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
        when(uriMock.toString()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
        when(uriMock.getQueryParameter(Constants.OAUTH.STATE_KEY)).thenReturn("wrong code");
        when(uriMock.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(CODE);
        //when
        presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock).finishWithStateKeyNotMatchingError();
    }

    @Test
    public void whenRedirectContainError_thenFinnishWithErrorReturn() {
        //given
        presenter.handleData("someUrl", STATE_KEY_CODE);
        String someError = "error";
        when(uriMock.toString()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
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
        when(uriMock.toString()).thenReturn(BuildConfig.DRIBBBLE_OAUTH_REDIRECT);
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
        when(uriMock.toString()).thenReturn("wrong redirect");
        //when
        boolean result = presenter.shouldOverrideUrlLoading(uriMock);
        //then
        verify(viewMock, never()).finishWithUnknownError();
        verify(viewMock, never()).finishWithError(anyString());
        verify(viewMock, never()).finishWithStateKeyNotMatchingError();
        verify(viewMock, never()).finishWithCodeReturn(CODE);
        Assert.assertFalse(result);
    }
}
