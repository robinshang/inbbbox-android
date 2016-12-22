package co.netguru.android.inbbbox.feature.follower;

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
import co.netguru.android.inbbbox.controller.ErrorController;
import co.netguru.android.inbbbox.controller.UserShotsController;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsContract;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsPresenter;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;

import static org.mockito.Matchers.any;
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
    User userMock;

    @InjectMocks
    FollowerDetailsPresenter followerDetailsPresenter;

    @Mock
    FollowerDetailsContract.View viewMock;

    @Before
    public void setUp() {
        followerDetailsPresenter.attachView(viewMock);
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();
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
