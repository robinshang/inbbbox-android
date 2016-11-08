package co.netguru.android.inbbbox.controller.authentication;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.model.api.Token;
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
public class ApiTokenControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public AuthorizeApi authorizeApiMock;

    @Mock
    public TokenPrefsRepository tokenPrefsRepository;

    @Mock
    public Resources resourcesMock;

    @InjectMocks
    public TokenController apiTokenController;

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
        when(tokenPrefsRepository.saveToken(expectedToken))
                .thenReturn(Completable.complete());

        apiTokenController.requestNewToken(CODE).subscribe(testSubscriber);

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

        apiTokenController.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedToken);
    }

    @Test
    public void whenGetTokenSubscriberFailed_thenReturnThrowable() {
        Throwable expectedThrowable = new Throwable("test");
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(expectedThrowable));

        apiTokenController.requestNewToken(CODE).subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
        verify(tokenPrefsRepository, never()).saveToken(anyObject());
    }
}