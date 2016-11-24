package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;
import android.support.v4.util.Pair;

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
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.OauthUrlController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import co.netguru.android.inbbbox.model.api.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    private OauthUrlController oauthUrlControllerMock;

    @Mock
    private ErrorMessageController errorMessageController;

    @Mock
    private TokenController tokenControllerMock;

    @Mock
    private LoginContract.View viewMock;

    @Mock
    private Uri uri;

    @Mock
    private UserController userControllerMock;

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
    }

    @Test
    public void whenLoginClick_thenShowActionViewForOauthRequest() {
        when(oauthUrlControllerMock.getOauthAuthorizeUrlAndUuidPair())
                .thenReturn(Observable.just(Pair.create(URL_STRING, UUID_STATIC)));

        presenter.showLoginView();

        verify(oauthUrlControllerMock, times(1)).getOauthAuthorizeUrlAndUuidPair();
        verify(viewMock, times((1))).openAuthWebViewFragment(URL_STRING, UUID_STATIC.toString());
        verify(viewMock, times(1)).disableLoginButton();
    }


    @Test
    public void whenCodeReceivedThen_getTokenUserAndFinish() {

        presenter.handleOauthCodeReceived(CODE);

        verify(userControllerMock, times(1)).requestUser();
        verify(tokenControllerMock).requestNewToken(CODE);
        verify(viewMock).showNextScreen();
    }

    @Test
    public void whenWebViewClose_thenEnableLoginButton() {
        presenter.handleWebViewClose();

        verify(viewMock, times(1)).enableLoginButton();
    }

    //Error
    @Test
    public void whenGettingErrorWhenTokenSaving_thenRunThroawbleMessageHanding() {
        String throwableText = "test";
        Throwable testThrowable = new Throwable(throwableText);
        when(tokenControllerMock.requestNewToken(CODE)).thenReturn(Observable.error(testThrowable));

        presenter.handleOauthCodeReceived(CODE);

        verify(viewMock, never()).showNextScreen();
        verify(viewMock).showApiError(anyString());
        verify(errorMessageController, times(1)).getErrorMessageLabel(testThrowable);
    }

}
