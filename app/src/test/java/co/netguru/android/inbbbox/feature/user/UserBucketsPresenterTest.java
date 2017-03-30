package co.netguru.android.inbbbox.feature.user;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.user.buckets.UserBucketsFragment;
import co.netguru.android.inbbbox.feature.user.buckets.UserBucketsPresenter;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Single;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserBucketsPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    UserBucketsFragment viewMock;

    @Mock
    BucketsController bucketsControllerMock;

    @Mock
    ErrorController errorControllerMock;

    UserBucketsPresenter presenter;

    User exampleUser = Statics.USER;

    BucketWithShots exampleBucket = BucketWithShots.create(Statics.BUCKET, Collections.emptyList());
    List<BucketWithShots> exampleBuckets = Arrays.asList(exampleBucket);

    Exception exampleException = new Exception("Exception");

    @Before
    public void setup() {
        presenter = new UserBucketsPresenter(bucketsControllerMock, errorControllerMock, exampleUser);
        presenter.attachView(viewMock);

        when(errorControllerMock.getThrowableMessage(exampleException))
                .thenReturn(exampleException.getMessage());
    }

    @Test
    public void whenLoadBucketsWithShotsThenGetBucketsFromController() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(exampleBuckets));

        presenter.loadBucketsWithShots(false);

        verify(bucketsControllerMock).getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void whenLoadBucketsWithShotsThenShowBuckets() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(exampleBuckets));

        presenter.loadBucketsWithShots(false);

        verify(viewMock).setData(exampleBuckets);
        verify(viewMock).showContent();
        verify(viewMock).hideProgressBars();
    }

    @Test
    public void whenLoadBucketsWithShotsErrorThenShowError() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.error(exampleException));

        presenter.loadBucketsWithShots(false);

        verify(viewMock).showMessageOnServerError(exampleException.getMessage());
    }

    @Test
    public void whenLoadMoreBucketsWithShotsThenGetMoreBucketsFromController() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(exampleBuckets));

        presenter.loadMoreBucketsWithShots();

        verify(bucketsControllerMock).getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void whenLoadMoreBucketsWithShotsErrorThenShowError() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.error(exampleException));

        presenter.loadMoreBucketsWithShots();

        verify(viewMock).showMessageOnServerError(exampleException.getMessage());
    }


    @Test
    public void whenLoadMoreBucketsWithShotsThenShowMoreBuckets() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(exampleBuckets));

        presenter.loadMoreBucketsWithShots();

        verify(viewMock).addMoreBucketsWithShots(exampleBuckets);
        verify(viewMock).hideLoadingMoreBucketsView();
        verify(viewMock).hideProgressBars();
    }

    @Test
    public void whenHandleBucketWithShotsClickThenOpenDetails() {
        presenter.handleBucketWithShotsClick(exampleBucket);

        verify(viewMock).showDetailedBucketView(eq(exampleBucket), anyInt());
    }

    @Test
    public void whenRefreshShotsThenLoadShotsWithoutCache() {
        when(bucketsControllerMock.getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Single.just(exampleBuckets));

        presenter.refreshBuckets();

        verify(bucketsControllerMock).getUserBucketsWithShots(anyLong(), anyInt(),
                anyInt(), anyInt(), eq(false));
    }

    @Test
    public void whenShowContentForEmptyDataThenShowEmptyView() {
        presenter.showContentForData(Collections.emptyList());

        verify(viewMock).showEmptyView();
    }

    @Test
    public void whenShowContentForDataThenHideEmptyView() {
        presenter.showContentForData(exampleBuckets);

        verify(viewMock).hideEmptyView();
    }

}
