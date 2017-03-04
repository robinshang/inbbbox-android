package co.netguru.android.inbbbox.feature.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserActivityPresenterTest {

    private final List<Shot> shotList = new ArrayList<>();
    private static final long USER_ID = 123;
    private static final boolean FOLLOWING = true;
    private static final String ERROR_MESSAGE = "error message";
    private static final String USERNAME = "username";

    @Rule
    public RxSyncTestRule rxSyncTestRule = new RxSyncTestRule();

    @InjectMocks
    UserActivityPresenter presenter;

    @Mock
    FollowersController followersController;

    @Mock
    ErrorController errorController;

    @Mock
    UserActivityContract.View view;

    @Mock
    User user;

    @Before
    public void setUp() throws Exception {
        presenter.attachView(view);
        when(user.id()).thenReturn(USER_ID);
        when(user.username()).thenReturn(USERNAME);
        when(errorController.getThrowableMessage(any(Throwable.class))).thenReturn(ERROR_MESSAGE);
    }

    @Test
    public void whenCheckFollowingStatus_thenFetchFollowingStatusAndShowInView()
            throws Exception {
        when(followersController.isUserFollowed(anyInt())).thenReturn(Single.just(FOLLOWING));

        presenter.checkFollowingStatus(user);

        verify(followersController).isUserFollowed(eq(USER_ID));
        verify(view).showFollowingAction(eq(!FOLLOWING));
        verifyNoMoreInteractions(view, followersController);
    }

    @Test
    public void whenStartFollowing_thenChangeIconAndMakeRequest() throws Exception {
        when(followersController.followUser(any(User.class))).thenReturn(Completable.complete());

        presenter.startFollowing(user);

        verify(followersController).followUser(eq(user));
        verify(view, times(2)).showFollowingAction(eq(false));
        verifyNoMoreInteractions(view, followersController);
    }

    @Test
    public void whenStopFollowing_thenChangeIconAndMakeRequest() throws Exception {
        when(followersController.unFollowUser(anyInt())).thenReturn(Completable.complete());

        presenter.stopFollowing(user);

        verify(followersController).unFollowUser(eq(USER_ID));
        verify(view, times(2)).showFollowingAction(eq(true));
        verifyNoMoreInteractions(view, followersController);
    }

    @Test
    public void whenCheckFollowingStatusError_thenShowMessage()
            throws Exception {
        when(followersController.isUserFollowed(anyInt()))
                .thenReturn(Single.error(new Throwable()));

        presenter.checkFollowingStatus(user);

        verify(followersController).isUserFollowed(eq(USER_ID));
        verify(view).showMessageOnServerError(anyString());
        verify(view).showFollowingAction(eq(true));
        verify(errorController).getThrowableMessage(any(Throwable.class));
        verifyNoMoreInteractions(view, followersController, errorController);
    }

    @Test
    public void whenStartFollowingError_thenRevertIconAndShowMessage() throws Exception {
        when(followersController.followUser(any(User.class)))
                .thenReturn(Completable.error(new Throwable()));

        presenter.startFollowing(user);

        verify(followersController).followUser(eq(user));
        verify(view).showFollowingAction(eq(false));
        verify(view).showFollowingAction(eq(true));
        verify(view).showMessageOnServerError(anyString());
        verify(errorController).getThrowableMessage(any(Throwable.class));
        verifyNoMoreInteractions(view, followersController, errorController);
    }

    @Test
    public void whenStopFollowingError_thenRevertIconAndShowMessage() throws Exception {
        when(followersController.unFollowUser(anyInt()))
                .thenReturn(Completable.error(new Throwable()));

        presenter.stopFollowing(user);

        verify(followersController).unFollowUser(eq(USER_ID));
        verify(view).showFollowingAction(eq(true));
        verify(view).showFollowingAction(eq(false));
        verify(view).showMessageOnServerError(anyString());
        verify(errorController).getThrowableMessage(any(Throwable.class));
        verifyNoMoreInteractions(view, followersController, errorController);
    }

    @Test
    public void givenUserUnfollowed_whenChangeFollowingStatus_thenCallStartFollowing() throws
            Exception {
        when(followersController.followUser(any(User.class)))
                .thenReturn(Completable.complete());

        presenter.changeFollowingStatus(user, true);

        verify(followersController).followUser(eq(user));
        verify(view, times(2)).showFollowingAction(eq(false));
        verifyNoMoreInteractions(view, followersController);
    }

    @Test
    public void givenUserFollowed_whenChangeFollowingStatus_thenShowDialog() throws
            Exception {
        when(followersController.followUser(any(User.class)))
                .thenReturn(Completable.error(new Throwable()));

        presenter.changeFollowingStatus(user, false);

        verify(view).showUnfollowDialog(eq(user.username()));
        verifyNoMoreInteractions(view, followersController);
    }

    @Test
    public void whenShareUser_thenCallShowShareFunction() {
        final String username = "test";
        when(user.username()).thenReturn(username);

        presenter.shareUser(user);

        verify(view).showShare(Matchers.contains(user.username()));
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView(false);
    }

}