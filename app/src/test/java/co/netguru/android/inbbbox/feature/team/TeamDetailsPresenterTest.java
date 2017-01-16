package co.netguru.android.inbbbox.feature.team;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamDetailsPresenterTest {

    private static final Long EXAMPLE_ID = 2L;
    private static final int USERS_PAGE_COUNT = 15;
    private static final int SHOTS_PER_USER = 4;
    private static final String EXAMPLE_NAME = "Żyraf";
    private final List<User> usersList = new ArrayList<>();
    private final List<UserWithShots> usersWithShotsList = new ArrayList<>();
    @Rule
    public TestRule rule = new RxSyncTestRule();
    @InjectMocks
    TeamDetailsPresenter presenter;
    @Mock
    TeamDetailsContract.View viewMock;
    @Mock
    UserWithShots teamMock;
    @Mock
    User userMock;
    @Mock
    TeamController teamControllerMock;
    @Mock
    FollowersController followersControllerMock;
    @Mock
    ErrorController errorControllerMock;
    @Mock
    UserShotsController userShotsControllerMock;

    @Before
    public void setUp() {
        presenter.attachView(viewMock);
        when(teamMock.user()).thenReturn(userMock);
        when(teamMock.user().id()).thenReturn(EXAMPLE_ID);
        when(teamMock.user().name()).thenReturn(EXAMPLE_NAME);

        User user = Statics.USER;
        usersList.add(user);

        UserWithShots userWithShots = UserWithShots.create(user, Statics.SHOT_LIST);
        usersWithShotsList.add(userWithShots);

        when(teamControllerMock.getTeamMembers(EXAMPLE_ID, 1, USERS_PAGE_COUNT)).thenReturn(Single.just(usersList));
        when(userShotsControllerMock.getUserShotsList(Statics.USER.id(), 1, SHOTS_PER_USER)).thenReturn(Observable.just(Statics.SHOT_LIST));
    }

    @Test
    public void whenDataLoadedCalled_thenRequestDataFromProvider() {

        presenter.loadTeamData(teamMock);

        verify(teamControllerMock, times(1)).getTeamMembers(EXAMPLE_ID, 1, USERS_PAGE_COUNT);
    }

    @Test
    public void whenDataLoadedCorrectly_thenHideLoadingIndicator() {

        presenter.loadTeamData(teamMock);

        verify(viewMock, times(1)).hideProgressBars();
    }

    @Test
    public void whenDataLoadedCorrectly_thenShowDownloadedItems() {

        presenter.loadTeamData(teamMock);

        verify(viewMock, times(1)).setData(usersWithShotsList);
    }

    @Test
    public void whenDataLoadingFailed_thenHideLoadingIndicator() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(userShotsControllerMock.getUserShotsList(Statics.USER.id(), 1, SHOTS_PER_USER)).thenReturn(Observable.error(exampleException));

        presenter.loadTeamData(teamMock);

        verify(viewMock, times(1)).hideProgressBars();
    }

    @Test
    public void whenDataLoadingFailed_thenShowErrorMessage() {
        String message = "test";
        Exception exampleException = new Exception(message);
        when(userShotsControllerMock.getUserShotsList(Statics.USER.id(), 1, SHOTS_PER_USER)).thenReturn(Observable.error(exampleException));
        when(errorControllerMock.getThrowableMessage(exampleException)).thenCallRealMethod();

        presenter.loadTeamData(teamMock);

        verify(viewMock, times(1)).showMessageOnServerError(message);
    }

    @Test
    public void whenCheckIfTeamIsFollowed_thenCallSetFollowingMenuIconWithTrue() {
        when(followersControllerMock.isUserFollowed(anyInt())).thenReturn(Single.just(Boolean.TRUE));

        presenter.checkIfTeamIsFollowed(UserWithShots.create(Statics.USER, null));

        verify(viewMock).setFollowingMenuIcon(Boolean.TRUE);
    }

    @Test
    public void whenCheckIfTeamIsNotFollowed_thenCallSetFollowingMenuIconWithFalse() {
        when(followersControllerMock.isUserFollowed(anyInt())).thenReturn(Single.just(Boolean.FALSE));

        presenter.checkIfTeamIsFollowed(UserWithShots.create(Statics.USER, null));

        verify(viewMock).setFollowingMenuIcon(Boolean.FALSE);
    }

    @Test
    public void whenOnUnfollowClick_thenShowUnfollowDialog() {
        presenter.loadTeamData(teamMock);
        presenter.onUnfollowClick();

        verify(viewMock).showUnfollowDialog(teamMock.user().name());
    }

    @Test
    public void whenOnFollowClick_thenSetFollowingIconToTrue() {
        when(followersControllerMock.followUser(teamMock.user())).thenReturn(Completable.complete());

        presenter.loadTeamData(teamMock);
        presenter.onFollowClick();

        verify(viewMock).setFollowingMenuIcon(true);
    }

    @Test
    public void whenOnUnfollow_thenSetFollowingIconToTrue() {
        when(followersControllerMock.unFollowUser(teamMock.user().id())).thenReturn(Completable.complete());

        presenter.loadTeamData(teamMock);
        presenter.unfollowUser();

        verify(viewMock).setFollowingMenuIcon(false);
    }

}