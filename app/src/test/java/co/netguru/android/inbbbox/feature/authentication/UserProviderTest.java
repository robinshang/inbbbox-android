package co.netguru.android.inbbbox.feature.authentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.testcommons.RxSyncTestRule;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.inbbbox.feature.testutils.TestUtils;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    UserApi userApiMock;

    @Mock
    DataSource<User> dataSourceMock;

    @InjectMocks
    UserProvider userProvider;

    @Test
    public void whenSubscribeToGetUser_thenSaveUserInStorage(){
        User expectedUser = new User();
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.just(expectedUser));
        TestSubscriber testSubscriber = new TestSubscriber();

        userProvider.getUser().subscribe(testSubscriber);

        verify(dataSourceMock).save(expectedUser);
    }

    @Test
    public void whenSubscribeToGetUser_thenPassTrue(){
        User expectedUser = new User();
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.just(expectedUser));
        when(dataSourceMock.save(expectedUser))
                .thenReturn(Observable.just(true));
        TestSubscriber testSubscriber = new TestSubscriber();

        userProvider.getUser().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(true);
    }

    //ERRORS
    @Test
    public void whenSubscribeToGetUserAndDownloadFailed_thenGetExceptionFromUser(){
        Throwable expectedThrowable = new Throwable("exception");
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.error(expectedThrowable));
        TestSubscriber testSubscriber = new TestSubscriber();

        userProvider.getUser().subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
    }

    @Test
    public void whenSubscribeToGerUserAndSavingFailed_thenGetExceptionFromDb(){
        Throwable expectedThrowable = new Throwable("exception");
        User expectedUser = new User();
        when(userApiMock.getAuthenticatedUser())
                .thenReturn(Observable.just(expectedUser));
        when(dataSourceMock.save(expectedUser))
                .thenReturn(Observable.error(expectedThrowable));
        TestSubscriber testSubscriber = new TestSubscriber();

        userProvider.getUser().subscribe(testSubscriber);

       testSubscriber.assertError(expectedThrowable);
    }


}