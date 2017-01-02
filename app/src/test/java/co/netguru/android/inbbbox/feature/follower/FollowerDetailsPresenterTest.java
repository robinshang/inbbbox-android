package co.netguru.android.inbbbox.feature.follower;

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
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsContract;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsPresenter;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FollowerDetailsPresenterTest {

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

    @Mock
    UserWithShots followerMock;

    @InjectMocks
    FollowerDetailsPresenter followerDetailsPresenter;

    @Mock
    FollowerDetailsContract.View viewMock;

    @Before
    public void setUp() {
        followerDetailsPresenter.attachView(viewMock);
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();
        when(followerMock.user()).thenReturn(userMock);

        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());

        when(followersControllerMock.isUserFollowed(anyLong())).thenReturn(Single.just(true));
    }

    @Test
    public void whenUserWithoutShotsReceived_thenDownloadUserUsingUserController() {
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.empty());
        when(userMock.id()).thenReturn(EXAMPLE_ID);
        when(followerMock.shotList()).thenReturn(null);

        followerDetailsPresenter.userDataReceived(followerMock);

        verify(userShotsControllerMock).getUserShotsList(eq(EXAMPLE_ID), anyInt(), anyInt());
    }

    @Test
    public void shouldShowUserDataWhenUserWithShotsReceived() {
        //given
        when(followerMock.shotList()).thenReturn(new LinkedList<>());
        //when
        followerDetailsPresenter.userDataReceived(followerMock);
        //then
        verify(viewMock).showFollowerData(any());
        verify(viewMock).showContent();
    }

    @Test
    public void shouldCallSetFollowingMenuIconWithTrueWhenUserIsFollowed() {
        //given
        when(followersControllerMock.isUserFollowed(anyInt())).thenReturn(Single.just(Boolean.TRUE));
        //when
        followerDetailsPresenter.checkIfUserIsFollowed(UserWithShots.create(Statics.USER, null));
        //then
        verify(viewMock).setFollowingMenuIcon(Boolean.TRUE);
    }

    @Test
    public void shouldCallSetFollowingMenuIconWithFalseWhenUserIsNotFollowed() {
        //given
        when(followersControllerMock.isUserFollowed(anyInt())).thenReturn(Single.just(Boolean.FALSE));
        //when
        followerDetailsPresenter.checkIfUserIsFollowed(UserWithShots.create(Statics.USER, null));
        //then
        verify(viewMock).setFollowingMenuIcon(Boolean.FALSE);
    }

    //ERRORS
    @Test
    public void whenUserReceivedAndUserDataDownloadFailed_thenShowError() {
        String message = "test";
        Throwable throwable = new Throwable(message);
        UserWithShots exampleUser = UserWithShots.create(Statics.USER, null);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.error(throwable));

        followerDetailsPresenter.userDataReceived(exampleUser);

        verify(viewMock).showMessageOnServerError(message);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaHooks.reset();
    }
}
