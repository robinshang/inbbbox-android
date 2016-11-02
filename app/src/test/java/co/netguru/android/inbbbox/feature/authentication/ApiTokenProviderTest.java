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

import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
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
    public DataSource<Token> dataSourceMock;

    @Mock
    public Resources resourcesMock;

    @InjectMocks
    public TokenProvider apiTokenProvider;

    private String code = "testCode";
    private String resourcesString = "clientId";
    private Token expectedToken = new Token();

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(resourcesString);
    }

    @Test
    public void whenGetTokenSubscribed_thenSaveTokenCalled() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(dataSourceMock.save(expectedToken))
                .thenReturn(Observable.just(true));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(dataSourceMock).save(expectedToken);
    }

    @Test
    public void whenGetTokenSubscribed_thenReturnTrue() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(expectedToken));
        when(dataSourceMock.save(expectedToken))
                .thenReturn(Observable.just(true));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(true);
    }

    @Test
    public void whenGetTokenSubscriberFailed_thenReturnThrowable() {
        Throwable expectedThrowable = new Throwable("test");
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber();
        when(authorizeApiMock.getToken(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(expectedThrowable));
        when(dataSourceMock.save(expectedToken))
                .thenReturn(Observable.just(false));

        apiTokenProvider.getToken(code).subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
        verify(dataSourceMock, never()).save(expectedToken);
    }
}