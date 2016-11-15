package co.netguru.android.inbbbox.feature.shots.addtobucket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddToBucketPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    BucketsController bucketsController;

    @Mock
    AddToBucketContract.View viewMock;

    @InjectMocks
    AddToBucketPresenter presenter;

    @Before
    public void setUp() {
        presenter.attachView(viewMock);
    }

    @Test
    public void whenShotWithTeamIsHandled_thenSetupView() {
        //given
        Shot shotWithTeam = Shot.builder().id(1).title("title").normalImageUrl("url").authorAvatarUrl("avatarurl")
                .authorName("name").team(Shot.Team.create(1, "some team")).isLiked(true).creationDate(LocalDateTime.now())
                .build();
        //when
        presenter.handleShot(shotWithTeam);
        //then
        verify(viewMock).setShotTitle(shotWithTeam.title());
        verify(viewMock).showShotPreview(shotWithTeam.normalImageUrl());
        verify(viewMock).showAuthorAvatar(shotWithTeam.authorAvatarUrl());
        verify(viewMock).showShotAuthorAndTeam(shotWithTeam.authorName(), shotWithTeam.team().name());
        verify(viewMock, never()).showShotAuthor(shotWithTeam.authorName());
        verify(viewMock).showShotCreationDate(shotWithTeam.creationDate());
    }

    @Test
    public void whenShotWithoutTeamIsHandled_thenSetupView() {
        //given
        Shot shotWithoutTeam = Shot.builder().id(1).title("title").normalImageUrl("url").authorAvatarUrl("avatarurl")
                .authorName("name").isLiked(true).creationDate(LocalDateTime.now())
                .build();
        //when
        presenter.handleShot(shotWithoutTeam);
        //then
        verify(viewMock).setShotTitle(shotWithoutTeam.title());
        verify(viewMock).showShotPreview(shotWithoutTeam.normalImageUrl());
        verify(viewMock).showAuthorAvatar(shotWithoutTeam.authorAvatarUrl());
        verify(viewMock, never()).showShotAuthorAndTeam(any(), any());
        verify(viewMock).showShotAuthor(shotWithoutTeam.authorName());
        verify(viewMock).showShotCreationDate(shotWithoutTeam.creationDate());
    }

    @Test
    public void whenLoadBucketsAndBucketsEmpty_thenShowEmptyView() {
        //given
        when(bucketsController.getCurrentUserBuckets()).thenReturn(Single.just(Collections.emptyList()));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).showNoBucketsAvailable();
    }

    @Test
    public void whenLoadBucketsAndBucketAvailable_thenShowBuckets() {
        //given
        List<Bucket> bucketList = Collections.singletonList(Statics.BUCKET);
        when(bucketsController.getCurrentUserBuckets()).thenReturn(Single.just(bucketList));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).showBuckets(bucketList);
    }

    @Test
    public void whenLoadBucketsAndErrorOccurs_thenShowError() {
        //given
        when(bucketsController.getCurrentUserBuckets()).thenReturn(Single.error(new Throwable()));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).showApiError();
    }

    @Test
    public void whenBucketSelected_thenPassResult() {
        //when
        presenter.handleBucketClick(Statics.BUCKET);
        //then
        verify(viewMock).passResultAndCloseFragment(Statics.BUCKET);
    }

}
