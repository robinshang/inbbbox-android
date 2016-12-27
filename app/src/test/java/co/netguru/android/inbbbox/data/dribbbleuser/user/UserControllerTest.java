package co.netguru.android.inbbbox.data.dribbbleuser.user;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    UserApi userApiMock;

    @Mock
    CurrentUserPrefsRepository currentUserPrefsRepository;

    @InjectMocks
    UserController userController;

    @Test
    public void whenSubscribeToGetUser_thenSaveUserInStorage() {
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.just(Statics.USER_ENTITY));
        when(currentUserPrefsRepository.saveUser(any())).thenReturn(Completable.complete());
        TestSubscriber<UserEntity> testSubscriber = new TestSubscriber<>();

        userController.requestUser().subscribe(testSubscriber);

        verify(currentUserPrefsRepository, times(1)).saveUser(Statics.USER_ENTITY);

        testSubscriber.assertValue(Statics.USER_ENTITY);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
    }

    //ERRORS
    @Test
    public void whenSubscribeToGetUserAndDownloadFailed_thenGetExceptionFromUser() {
        Throwable expectedThrowable = new Throwable("exception");
        when(userApiMock.getAuthenticatedUser()).thenReturn(Observable.error(expectedThrowable));
        TestSubscriber<UserEntity> testSubscriber = new TestSubscriber<>();

        userController.requestUser().subscribe(testSubscriber);

        testSubscriber.assertError(expectedThrowable);
    }

}