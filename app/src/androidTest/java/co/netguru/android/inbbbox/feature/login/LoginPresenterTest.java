package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.utils.ApiErrorParser;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginPresenterTest {

    private OauthUriProvider oauthUriProviderMock = mock(OauthUriProvider.class);
    private ApiErrorParser apiErrorParserMock = mock(ApiErrorParser.class);
    private ApiTokenProvider tokenProvider = mock(ApiTokenProvider.class);
    private LoginContract.View viewMock = mock(LoginContract.View.class);

    public LoginPresenter presenter;

    @Before
    public void setup(){
        presenter = new LoginPresenter(oauthUriProviderMock, tokenProvider, apiErrorParserMock);
        presenter.attachView(viewMock);
    }

    @Test
    public void whenLoginClick_thenGetUriFromUriProviderTest(){
        Uri uri = Uri.parse("http://google.com");

        when(oauthUriProviderMock.getOauthAutorizeUri()).thenReturn(Observable.just(uri));
        presenter.showLoginView();

        verify(viewMock).sendActionIntent(uri);
        Assert.assertEquals(true, true);
    }
}
