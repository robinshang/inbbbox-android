package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
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

    @Mock
    List<Bucket> bucketListMock;

    @InjectMocks
    RemoveFromBucketPresenter removeFromBucketPresenter;

    @Captor
    ArgumentCaptor<List<Bucket>> listArgumentCaptor;

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

        when(bucketsControllerMock.getListBucketsForShot(anyLong()))
                .thenReturn(Single.just(bucketListMock));

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

    @Test
    public void whenShotIsHandledAndShowFullScreenClicked_thenShowShotOnFullScreen() {
        removeFromBucketPresenter.handleShot(shotMock);

        removeFromBucketPresenter.onOpenShotFullscreen();

        verify(viewMock).openShotFullscreen(shotMock);
    }

    @Test
    public void whenBucketsSelected_thenRemoveSelectedBuckets() {
        Random random = new Random();
        List<Bucket> buckets = new ArrayList<>();
        for (int i = 2; i < random.nextInt(200); i++) {
            removeFromBucketPresenter.handleCheckboxClick(bucketMock, true);
            buckets.add(bucketMock);
        }
        removeFromBucketPresenter.handleShot(shotMock);

        removeFromBucketPresenter.removeShotFromBuckets();

        verify(viewMock).passResultAndCloseFragment(buckets, shotMock);
    }

    @Test
    public void whenBucketsRemoved_thenRemoveSelectedBuckets() {
        List<Bucket> buckets = new ArrayList<>();
        removeFromBucketPresenter.handleCheckboxClick(bucketMock, true);
        removeFromBucketPresenter.handleCheckboxClick(bucketMock, false);
        removeFromBucketPresenter.handleShot(shotMock);

        removeFromBucketPresenter.removeShotFromBuckets();

        verify(viewMock).passResultAndCloseFragment(buckets, shotMock);
    }

    @Test
    public void whenLoadBucketsCalled_thenRequestBuckets() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);

        removeFromBucketPresenter.loadBucketsForShot();

        verify(bucketsControllerMock).getListBucketsForShot(id);
    }

    @Test
    public void whenLoadBucketsCalled_thenShowProgressIndicator() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);

        removeFromBucketPresenter.loadBucketsForShot();

        verify(viewMock, atLeastOnce()).showBucketListLoading();
    }

    @Test
    public void whenLoadBucketsDone_thenHideProgressIndicator() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);

        removeFromBucketPresenter.loadBucketsForShot();

        verify(viewMock, atLeastOnce()).hideProgressBar();
    }

    @Test
    public void whenLoadBucketsDoneAndBucketsListReadWithManyItems_thenShowList() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);
        when(bucketListMock.size()).thenReturn(5);
        when(bucketListMock.get(0)).thenReturn(bucketMock);

        removeFromBucketPresenter.loadBucketsForShot();

        verify(viewMock).setBucketsList(anyListOf(Bucket.class));
        verify(viewMock).showBucketsList();
    }

    @Test
    public void whenLoadBucketsFailed_thenShowError() {
        String text = "test";
        Throwable throwable = new Throwable(text);
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);
        when(bucketListMock.size()).thenReturn(5);
        when(bucketListMock.get(0)).thenReturn(bucketMock);
        when(bucketsControllerMock.getListBucketsForShot(anyLong()))
                .thenReturn(Single.error(throwable));

        removeFromBucketPresenter.loadBucketsForShot();

        verify(viewMock).showMessageOnServerError(text);
    }

    @Test
    public void whenLoadMoreBucketsCalled_thenRequestBuckets() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);

        removeFromBucketPresenter.loadMoreBuckets();

        verify(bucketsControllerMock).getListBucketsForShot(id);
    }

    @Test
    public void whenLoadMoreBucketsDone_thenHideProgressIndicator() {
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);
        when(bucketListMock.size()).thenReturn(50);
        removeFromBucketPresenter.loadBucketsForShot();

        removeFromBucketPresenter.loadMoreBuckets();

        verify(viewMock, atLeastOnce()).hideProgressBar();
    }

    @Test
    public void whenLoadMoreBucketsFailed_thenShowError() {
        String text = "test";
        Throwable throwable = new Throwable(text);
        long id = 999;
        removeFromBucketPresenter.handleShot(shotMock);
        when(shotMock.id()).thenReturn(id);
        when(bucketListMock.size()).thenReturn(50);
        when(bucketListMock.get(0)).thenReturn(bucketMock);
        when(bucketsControllerMock.getListBucketsForShot(anyLong()))
                .thenReturn(Single.error(throwable));
        removeFromBucketPresenter.loadBucketsForShot();

        removeFromBucketPresenter.loadMoreBuckets();

        verify(viewMock).showMessageOnServerError(text);
    }

    @After
    public void tearDown() {
        removeFromBucketPresenter.detachView(false);
    }
}