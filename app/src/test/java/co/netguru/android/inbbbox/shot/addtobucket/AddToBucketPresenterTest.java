package co.netguru.android.inbbbox.shot.addtobucket;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.data.bucket.BucketsController;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.event.events.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddToBucketPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    RxBus rxBusMock;

    @Mock
    ErrorController errorControllerMock;

    @Mock
    AddToBucketContract.View viewMock;

    @InjectMocks
    AddToBucketPresenter presenter;

    @Test
    public void whenShotWithTeamIsHandled_thenSetupView() {
        setupWithEmptyRxBus();
        //given
        Shot shotWithTeam = Shot.update(Statics.LIKED_SHOT)
                .author(User.create(Statics.USER_ENTITY))
                .team(Statics.TEAM)
                .build();
        //when
        presenter.handleShot(shotWithTeam);
        //then
        verify(viewMock).setShotTitle(shotWithTeam.title());
        verify(viewMock).showShotPreview(shotWithTeam.normalImageUrl());
        verify(viewMock).showAuthorAvatar(shotWithTeam.author().avatarUrl());
        verify(viewMock).showShotAuthorAndTeam(shotWithTeam.author().name(), shotWithTeam.team().name());
        verify(viewMock, never()).showShotAuthor(shotWithTeam.author().name());
        verify(viewMock).showShotCreationDate(shotWithTeam.creationDate());
    }

    @Test
    public void whenShotWithoutTeamIsHandled_thenSetupView() {
        setupWithEmptyRxBus();
        //given
        Shot shotWithoutTeam = Shot.update(Statics.LIKED_SHOT)
                .author(User.create(Statics.USER_ENTITY))
                .team(null)
                .build();
        presenter.handleShot(shotWithoutTeam);
        //then
        verify(viewMock).setShotTitle(shotWithoutTeam.title());
        verify(viewMock).showShotPreview(shotWithoutTeam.normalImageUrl());
        verify(viewMock).showAuthorAvatar(shotWithoutTeam.author().avatarUrl());
        verify(viewMock, never()).showShotAuthorAndTeam(any(), any());
        verify(viewMock).showShotAuthor(shotWithoutTeam.author().name());
        verify(viewMock).showShotCreationDate(shotWithoutTeam.creationDate());
    }

    @Test
    public void whenLoadBucketsAndBucketsEmpty_thenShowEmptyView() {
        setupWithEmptyRxBus();
        //given
        when(bucketsControllerMock.getCurrentUserBuckets(anyInt(), anyInt())).thenReturn(Single.just(Collections.emptyList()));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).showNoBucketsAvailable();
    }

    @Test
    public void whenLoadBucketsAndBucketAvailable_thenShowBuckets() {
        setupWithEmptyRxBus();
        //given
        List<Bucket> bucketList = Collections.singletonList(Statics.BUCKET);
        when(bucketsControllerMock.getCurrentUserBuckets(anyInt(), anyInt())).thenReturn(Single.just(bucketList));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).setBucketsList(bucketList);
    }

    @Test
    public void whenLoadBucketsAndErrorOccurs_thenShowError() {
        setupWithEmptyRxBus();
        //given
        when(bucketsControllerMock.getCurrentUserBuckets(anyInt(), anyInt())).thenReturn(Single.error(new Throwable()));
        //when
        presenter.loadAvailableBuckets();
        //then
        verify(viewMock).showBucketListLoading();
        verify(viewMock).showMessageOnServerError(anyString());
    }

    @Test
    public void whenBucketSelected_thenPassResult() {
        setupWithEmptyRxBus();
        //given
        presenter.handleShot(Statics.LIKED_SHOT);
        //when
        presenter.handleBucketClick(Statics.BUCKET);
        //then
        verify(viewMock).passResultAndCloseFragment(Statics.BUCKET, Statics.LIKED_SHOT);
    }

    @Test
    public void whenRxBusBucketCreatedEventReceived_thenAddBucket() {
        //given
        when(rxBusMock.getEvents(any())).thenReturn(Observable.just(new BucketCreatedEvent(Statics.BUCKET)));
        //when
        presenter.attachView(viewMock);
        //then
        verify(viewMock, times(1)).addNewBucketOnTop(Statics.BUCKET);
        verify(viewMock, times(1)).showBucketsList();
        verify(viewMock, times(1)).scrollToTop();
    }

    private void setupWithEmptyRxBus() {
        when(rxBusMock.getEvents(any())).thenReturn(Observable.empty());
        presenter.attachView(viewMock);
    }

}
