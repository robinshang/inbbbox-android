package co.netguru.android.inbbbox.controller.authentication;

import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.session.AuthorizeApi;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.session.TokenPrefsRepository;
import co.netguru.android.inbbbox.data.session.model.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public AuthorizeApi authorizeApiMock;

    @Mock
    public TokenPrefsRepository tokenPrefsRepository;

    @Mock
    public Resources resourcesMock;

    @Mock
    public Token tokenMock;

    @InjectMocks
    public TokenController tokenController;

    private static final String CODE = "testCode";
    private static final String RESOURCES_STRING = "clientId";

    private Token expectedToken;

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(RESOURCES_STRING);
        when(tokenPrefsRepository.getToken()).thenReturn(tokenMock);
        expectedToken = new Token("", "", "");
    }

    @Test
    public void whenGetTokenSubscribed_thenSaveTokenCalled() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(tokenPrefsRepository.saveToken(expectedToken))
                .thenReturn(Completable.complete());

        tokenController.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        verify(tokenPrefsRepository).saveToken(expectedToken);
    }

    @Test
    public void whenGetTokenSubscribed_thenReturnTrue() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(tokenPrefsRepository.saveToken(expectedToken))
                .thenReturn(Completable.complete());

        tokenController.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedToken);
    }

    @Test
    public void whenGetTokenSubscriberFailed_thenReturnThrowable() {
        Throwable expectedThrowable = new Throwable("test");
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(expectedThrowable));

        tokenController.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
        verify(tokenPrefsRepository, never()).saveToken(anyObject());
    }

    @Test
    public void whenTokenExist_thenReturnTrueWhenTokenIsValid() {
        String exampleToken = "token";
        when(tokenMock.getAccessToken()).thenReturn(exampleToken);
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        tokenController.isTokenValid().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(true, testSubscriber.getOnNextEvents().get(0));
    }

    @Test
    public void whenTokenNotExist_thenReturnFalseWhenTokenIsValid() {
        String exampleToken = null;
        when(tokenMock.getAccessToken()).thenReturn(exampleToken);
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        tokenController.isTokenValid().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(false, testSubscriber.getOnNextEvents().get(0));
    }

    @Test
    public void whenTokenSaveIsSubscribed_thenCallSaveMethodOnRepository() {

        tokenController.saveToken(tokenMock);

        verify(tokenPrefsRepository, times(1)).saveToken(tokenMock);
    }
}