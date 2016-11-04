package co.netguru.android.inbbbox.feature.authentication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.data.local.UserPrefsController;
import co.netguru.android.inbbbox.feature.Statics;
import co.netguru.android.inbbbox.models.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    UserApi userApiMock;

    @Mock
    UserPrefsController userPrefsController;

    @InjectMocks
    UserProvider userProvider;

    @Test
    public void whenSubscribeToGetUser_thenSaveUserInStorage() {
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.just(Statics.USER));
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userProvider.requestUser().subscribe(testSubscriber);

        verify(userPrefsController, times(1)).saveUser(Statics.USER);

        testSubscriber.assertValue(Statics.USER);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
    }

    //ERRORS
    @Test
    public void whenSubscribeToGetUserAndDownloadFailed_thenGetExceptionFromUser() {
        Throwable expectedThrowable = new Throwable("exception");
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.error(expectedThrowable));
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userProvider.requestUser().subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
    }

}