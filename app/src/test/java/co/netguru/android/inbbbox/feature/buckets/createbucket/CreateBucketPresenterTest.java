package co.netguru.android.inbbbox.feature.buckets.createbucket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controller.BucketsController;
import co.netguru.android.inbbbox.event.events.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.feature.bucket.createbucket.CreateBucketContract;
import co.netguru.android.inbbbox.feature.bucket.createbucket.CreateBucketPresenter;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateBucketPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    BucketsController bucketsController;

    @Mock
    CreateBucketContract.View view;

    @Mock
    RxBus rxBus;

    @InjectMocks
    CreateBucketPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter.attachView(view);
    }

    @Test
    public void whenHandleCreateBucketWithNotEmptyNameString_thenCreateBucket() {
        //given
        String bucketName = "Bucket";
        String description = null;
        when(bucketsController.createBucket(anyString(), anyString())).thenReturn(Single.just(Statics.BUCKET));
        //when
        presenter.handleCreateBucket(bucketName, description);
        //then
        verify(view, times(1)).hideErrorMessages();

        verify(view, times(1)).showProgressView();
        verify(view, times(1)).hideProgressView();
        verify(view, times(1)).close();
        verify(view, never()).showErrorEmptyBucket();
        verify(rxBus,times(1)).send(any(BucketCreatedEvent.class));
    }

    @Test
    public void whenHandleCreateBucketWithEmptyString_thenShowEmptyError() {
        //given
        String bucketName = "";
        String description = null;
        when(bucketsController.createBucket(anyString(), anyString())).thenReturn(Single.just(Statics.BUCKET));
        //when
        presenter.handleCreateBucket(bucketName, description);
        //then
        verify(view, times(1)).hideErrorMessages();

        verify(view, never()).showProgressView();
        verify(view, never()).hideProgressView();
        verify(view, never()).close();
        verify(rxBus,never()).send(any(BucketCreatedEvent.class));
        verify(view, times(1)).showErrorEmptyBucket();
    }

}