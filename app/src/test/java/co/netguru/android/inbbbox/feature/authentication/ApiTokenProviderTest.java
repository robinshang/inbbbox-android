package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.local.TokenPrefsController;
import co.netguru.android.inbbbox.models.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiTokenProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public AuthorizeApi authorizeApiMock;

    @Mock
    public TokenPrefsController tokenPrefsController;

    @Mock
    public Resources resourcesMock;

    @InjectMocks
    public TokenProvider apiTokenProvider;

    private static final String CODE = "testCode";
    private static final String RESOURCES_STRING = "clientId";

    private Token expectedToken;

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(RESOURCES_STRING);
        expectedToken = new Token("", "", "");
    }

    @Test
    public void whenGetTokenSubscribed_thenSaveTokenCalled() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(tokenPrefsController.saveToken(expectedToken))
                .thenReturn(Completable.complete());

        apiTokenProvider.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        verify(tokenPrefsController).saveToken(expectedToken);
    }

    @Test
    public void whenGetTokenSubscribed_thenReturnTrue() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(tokenPrefsController.saveToken(expectedToken))
                .thenReturn(Completable.complete());

        apiTokenProvider.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedToken);
    }

    @Test
    public void whenGetTokenSubscriberFailed_thenReturnThrowable() {
        Throwable expectedThrowable = new Throwable("test");
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(expectedThrowable));

        apiTokenProvider.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
        verify(tokenPrefsController, never()).saveToken(anyObject());
    }
}