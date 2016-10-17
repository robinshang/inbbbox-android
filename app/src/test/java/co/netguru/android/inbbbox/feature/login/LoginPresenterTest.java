package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.meta.When;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaCompletableExecutionHook;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    private OauthUriProvider oauthUriProviderMock;

    @Mock
    private ErrorMessageParser apiErrorParserMock;

    @Mock
    private ApiTokenProvider tokenProviderMock;

    @Mock
    private LoginContract.View viewMock;

    @Mock
    private UserProvider userProviderMock;

    @Mock
    public Uri uri;

    private String code = "testCode";
    private String errorLabel = "testError";

    @InjectMocks
    private LoginPresenter presenter;

    @Before
    public void setup() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        presenter.attachView(viewMock);
        when(tokenProviderMock.getToken(code)).thenReturn(Observable.just(true));
        when(userProviderMock.getUser()).
                thenReturn(Observable.just(new User()));
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void whenLoginClick_thenGetUriFromUriProviderTest() {
        when(oauthUriProviderMock.getOauthAutorizeUri()).thenReturn(Observable.just(uri));

        presenter.showLoginView();

        verify(viewMock).sendActionIntent(uri);
    }

    @Test
    public void whenLoginClick_thenShowActionViewForOauthRequest() {
        when(oauthUriProviderMock.getOauthAutorizeUri()).thenReturn(Observable.just(uri));

        presenter.showLoginView();

        verify(viewMock, times((1))).sendActionIntent(uri);
    }

    @Test
    public void whenRedirectUriFromActionViewDetected_sendAuthorizationRequestWithReceivedCode() {
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(tokenProviderMock, times(1)).getToken(code);
    }

    @Test
    public void whenTokenReceived_thenGetUser() {
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(userProviderMock, times(1)).getUser();
    }

    @Test
    public void whenUserReceived_thenShowMainView() {
        String code = "testCode";
        when(uri.getQueryParameter(Constants.OAUTH.CODE_KEY)).thenReturn(code);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock, times(1)).showNextScreen();
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
    public void  whenHandlingOauthResponseWithoutCode_thenShowReceivedError() {
        String errorMessage = "test error";
        when(uri.getQueryParameter(Constants.OAUTH.ERROR_KEY)).thenReturn(errorMessage);

        presenter.handleOauthLoginResponse(uri);

        verify(viewMock).showApiError(errorMessage);
    }

}
