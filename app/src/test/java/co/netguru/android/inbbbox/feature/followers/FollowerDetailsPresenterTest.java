package co.netguru.android.inbbbox.feature.followers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.followers.details.FollowerDetailsContract;
import co.netguru.android.inbbbox.feature.followers.details.FollowerDetailsPresenter;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Response;
import rx.Completable;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
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
    Follower followerMock;

    @InjectMocks
    FollowerDetailsPresenter followerDetailsPresenter;

    @Mock
    FollowerDetailsContract.View viewMock;

    @Before
    public void setUp() {
        followerDetailsPresenter.attachView(viewMock);
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();

        okhttp3.Response rawResponse = new okhttp3.Response.Builder()
                .code(204)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build();
        Response<Completable> response = Response.success(Completable.complete(), rawResponse);
        when(followersControllerMock.checkIfUserIsFollowed(anyLong())).thenReturn(Observable.just(response));
    }

    @Test
    public void whenUserReceived_thenDownloadUserUsingUserController() {
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.empty());
        when(userMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.userDataReceived(userMock);

        verify(userShotsControllerMock, times(1))
                .getUserShotsList(eq(EXAMPLE_ID), anyInt(), anyInt());
    }

    @Test
    public void whenUserReceived_thenCheckIfIsFollowed() {
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.empty());
        when(userMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.userDataReceived(userMock);

        verify(followersControllerMock, times(1))
                .checkIfUserIsFollowed(eq(EXAMPLE_ID));
    }

    @Test
    public void whenUserReceivedAndCheckedIfIsFollowed_thenSetMenuIcon() {
        User exampleUser = User.create(Statics.USER_ENTITY);
        List<Shot> listOfShots = Arrays.asList(Statics.LIKED_SHOT, Statics.NOT_LIKED_SHOT);

        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.just(listOfShots));
        when(userMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.userDataReceived(exampleUser);

        verify(viewMock, times(1)).setMenuIcon(anyBoolean());
    }

    @Test
    public void whenFollowerReceived_thenCheckIfIsFollowed() {
        when(followerMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.followerDataReceived(followerMock);

        verify(followersControllerMock, times(1))
                .checkIfUserIsFollowed(eq(EXAMPLE_ID));
    }

    @Test
    public void whenFollowerReceivedAndCheckedIfIsFollowed_thenSetMenuIcon() {
        User exampleUser = User.create(Statics.USER_ENTITY);
        List<Shot> listOfShots = Arrays.asList(Statics.LIKED_SHOT, Statics.NOT_LIKED_SHOT);

        when(followerMock.id()).thenReturn(EXAMPLE_ID);

        followerDetailsPresenter.followerDataReceived(Follower.createFromUser(exampleUser, listOfShots));

        verify(viewMock, times(1)).setMenuIcon(anyBoolean());
    }

    @Test
    public void whenUserReceivedAndUserDataDownloadComplete_thenCreateAndShowFollower() {
        User exampleUser = User.create(Statics.USER_ENTITY);
        List<Shot> listOfShots = Arrays.asList(Statics.LIKED_SHOT, Statics.NOT_LIKED_SHOT);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.just(listOfShots));
        ArgumentCaptor<Follower> argumentCaptor = ArgumentCaptor.forClass(Follower.class);

        followerDetailsPresenter.userDataReceived(exampleUser);

        verify(viewMock, times(1)).showFollowerData(argumentCaptor.capture());
        Assert.assertEquals(EXAMPLE_ID, argumentCaptor.getValue().id());
        Assert.assertEquals(listOfShots, argumentCaptor.getValue().shotList());
    }

    //ERRORS
    @Test
    public void whenUserReceivedAndUserDataDownloadFailed_thenShowError() {
        String message = "test";
        Throwable throwable = new Throwable(message);
        User exampleUser = User.create(Statics.USER_ENTITY);
        when(userShotsControllerMock.getUserShotsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(Observable.error(throwable));

        followerDetailsPresenter.userDataReceived(exampleUser);

        verify(viewMock, times(1)).showMessageOnServerError(message);
    }
}
