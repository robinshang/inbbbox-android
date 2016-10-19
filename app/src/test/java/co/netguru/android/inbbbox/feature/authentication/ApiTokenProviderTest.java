package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.feature.testutils.TestUtils;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiTokenProviderTest extends TestUtils {

    @Mock
    public AuthorizeApi authorizeApiMock;

    @Mock
    public CacheEndpoint cacheEndpointMock;

    @Mock
    public Resources resourcesMock;

    @InjectMocks
    public ApiTokenProvider apiTokenProvider;

    private String code = "testCode";
    private String resourcesString = "clientId";
    private Token expectedToken = new Token();
    private String expectedTokenKey = Constants.Db.TOKEN_KEY;

    @Before
    public void setUp() {
        setupJavaThreadingManagementForTests();
        when(resourcesMock.getString(anyInt())).thenReturn(resourcesString);

    }

    @After
    public void tearDown() {
        resetJavaThreading();
    }

    @Test
    public void whenGetTokenSubscribed_thenSaveTokenCalled() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(cacheEndpointMock.save(expectedTokenKey, expectedToken))
                .thenReturn(Observable.just(expectedToken));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(cacheEndpointMock).save(expectedTokenKey, expectedToken);
    }

    @Test
    public void whenGetTokenSubscribed_thenReturnTokenInstance() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(cacheEndpointMock.save(expectedTokenKey, expectedToken))
                .thenReturn(Observable.just(expectedToken));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedToken);
    }

    @Test
    public void whenGetTokenSubscriberFailed_thenReturnThrowable() {
        Throwable expectedThrowable = new Throwable("test");
        TestSubscriber<Token> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(expectedThrowable));
        when(cacheEndpointMock.save(expectedTokenKey, expectedToken))
                .thenReturn(Observable.just(expectedToken));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
        verify(cacheEndpointMock, never()).save(expectedTokenKey, expectedToken);
    }
}