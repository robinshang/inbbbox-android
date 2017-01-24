package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoveFromBucketPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ErrorController errorControllerMock;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    Shot shotMock;

    @Mock
    Bucket bucketMock;

    @Mock
    Team teamMock;

    @Mock
    User authorMock;

    @Mock
    RemoveFromBucketContract.View viewMock;

    @InjectMocks
    RemoveFromBucketPresenter removeFromBucketPresenter;

    private String name = "name";
    private String url = "url";

    @Before
    public void setup() {
        when(errorControllerMock.getThrowableMessage(any(Throwable.class))).thenCallRealMethod();
        when(errorControllerMock.getMessageBasedOnErrorCode(anyInt())).thenCallRealMethod();

        when(shotMock.title()).thenReturn(name);
        when(shotMock.normalImageUrl()).thenReturn(url);
        when(shotMock.author()).thenReturn(authorMock);
        when(authorMock.avatarUrl()).thenReturn(url);

        removeFromBucketPresenter.attachView(viewMock);
    }

    @Test
    public void whenHandleShot_thenShowShotTittlePreviewAndAuthorAvatarAndCreationDate() {
        ZonedDateTime now = ZonedDateTime.now();
        when(shotMock.creationDate()).thenReturn(now);

        removeFromBucketPresenter.handleShot(shotMock);

        verify(viewMock).setShotTitle(name);
        verify(viewMock).showShotPreview(url);
        verify(viewMock).showAuthorAvatar(url);
        verify(viewMock).showShotCreationDate(now);
    }

    @Test
    public void whenHandleShotAndThereIsNoTeam_thenShowOnlyAuthor() {
        String authorName = "authorName";
        when(shotMock.team()).thenReturn(null);
        when(authorMock.name()).thenReturn(authorName);

        removeFromBucketPresenter.handleShot(shotMock);

        verify(viewMock).showShotAuthor(authorName);
    }

    @Test
    public void whenHandleShotAndThereIsTeamSet_thenShowAuthorAndTeam() {
        String authorName = "authorName";
        String teamName = "team";
        when(shotMock.team()).thenReturn(teamMock);
        when(authorMock.name()).thenReturn(authorName);
        when(teamMock.name()).thenReturn(teamName);

        removeFromBucketPresenter.handleShot(shotMock);

        verify(viewMock).showShotAuthorAndTeam(authorName, teamName);
    }

    @After
    public void tearDown() {
        removeFromBucketPresenter.detachView(false);
    }
}