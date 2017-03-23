package co.netguru.android.inbbbox.feature.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsContract;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsPresenter;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPresenterTest {

    private static final long EXAMPLE_ID = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    UserShotsController userShotsControllerMock;

    @Mock
    ErrorController errorControllerMock;

    @Mock
    FollowersController followersControllerMock;

    @Mock
    User userMock;

    @InjectMocks
    UserShotsPresenter followerDetailsPresenter;

    @Mock
    UserShotsContract.View viewMock;

    @Before
    public void setUp() {
        followerDetailsPresenter.attachView(viewMock);
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();

        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());

        when(followersControllerMock.isUserFollowed(anyLong())).thenReturn(Single.just(true));
    }

    @Test
    public void whenUserWithoutShotsReceived_thenDownloadUserUsingUserController() {
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Observable.empty());
        when(userMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.userDataReceived(userMock);

        verify(userShotsControllerMock).getUserShotsList(eq(EXAMPLE_ID), anyInt(), anyInt(), anyBoolean());
    }

    //ERRORS
    @Test
    public void whenUserReceivedAndUserDataDownloadFailed_thenShowError() {
        String message = "test";
        Throwable throwable = new Throwable(message);
        UserWithShots exampleUser = UserWithShots.create(Statics.USER, null);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Observable.error(throwable));

        followerDetailsPresenter.userDataReceived(userMock);

        verify(viewMock).showMessageOnServerError(message);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaHooks.reset();
    }
}
