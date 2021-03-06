package co.netguru.android.inbbbox.feature.bucket.detail;

import org.junit.Before;
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
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;
import rx.subscriptions.Subscriptions;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucketDetailsPresenterTest {

    private static final List<Shot> shots = Statics.generateShots();
    private static final BucketWithShots BUCKET_WITH_SHOTS = BucketWithShots.create(Statics.BUCKET, shots);
    private static final int PER_PAGE = BUCKET_WITH_SHOTS.shots().size();

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    BucketDetailsContract.View view;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    RxBus rxBusMock;

    @InjectMocks
    BucketDetailsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        when(rxBusMock.getEvents(any())).thenReturn(Observable.empty());
        presenter.attachView(view);
    }

    @Test
    public void detachView() throws Exception {
        //given
        TestSubscriber refreshTestSubscriber = TestSubscriber.create();
        TestSubscriber loadMoreTestSubscriber = TestSubscriber.create();
        presenter.refreshShotsSubscription = refreshTestSubscriber;
        presenter.loadNextShotsSubscription = loadMoreTestSubscriber;
        //when
        presenter.detachView(false);
        //then
        loadMoreTestSubscriber.assertUnsubscribed();
        loadMoreTestSubscriber.assertUnsubscribed();
    }

    @Test
    public void whenNewDataPassedWithNotEmptyListOfShots_thenSetupViewProperly() throws Exception {
        //given
        BucketWithShots bucketWithShots = BucketWithShots.create(Statics.BUCKET, shots);
        final int perPage = 30;
        //when
        presenter.handleBucketData(bucketWithShots, perPage);
        //then
        verify(view, times(1)).setFragmentTitle(bucketWithShots.bucket().name());
        verify(view, times(1)).setData(shots);
        verify(view, never()).showEmptyView();
    }

    @Test
    public void whenNewDataPassedWithEmptyListOfShots_thenSetupViewProperly() throws Exception {
        //given
        BucketWithShots bucketWIthNoShots = BucketWithShots.create(Statics.BUCKET, Collections.emptyList());
        //when
        presenter.handleBucketData(bucketWIthNoShots, PER_PAGE);
        //then
        verify(view, times(1)).setFragmentTitle(BUCKET_WITH_SHOTS.bucket().name());
        verify(view, never()).setData(shots);
        verify(view, times(1)).showContent();
    }

    @Test
    public void whenRefreshCalledAndNextRequestInProgress_thenStopAndLoadNewData() throws Exception {
        //given
        TestSubscriber loadNextShotsSubscription = new TestSubscriber();
        presenter.refreshShotsSubscription = Subscriptions.unsubscribed();
        presenter.loadNextShotsSubscription = loadNextShotsSubscription;
        when(bucketsControllerMock.getShotsFromBucket(anyLong(), anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(shots));
        //when
        presenter.refreshShots();
        //then
        loadNextShotsSubscription.assertUnsubscribed();
        verify(view, times(1)).hideProgressbar();
        verify(view, times(1)).setData(any(List.class));
        verify(view, never()).addShots(any(List.class));
    }

    @Test
    public void whenRemoveBucketButtonIsClicked_thenShowDialog() {
        //when
        presenter.onDeleteBucketClick();
        //then
        verify(view, times(1)).showRemoveBucketDialog(anyString());
    }

    @Test
    public void whenDeleteBucket_thenShowBucketView() throws Exception {
        //given
        when(bucketsControllerMock.deleteBucket(anyLong())).thenReturn(Completable.complete());
        //when
        presenter.handleBucketData(BUCKET_WITH_SHOTS, PER_PAGE);
        presenter.deleteBucket();
        //then
        verify(view, times(1)).showRefreshedBucketsView(BUCKET_WITH_SHOTS.bucket().id());

    }
}