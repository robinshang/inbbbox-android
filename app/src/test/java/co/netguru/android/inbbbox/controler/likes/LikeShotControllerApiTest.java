package co.netguru.android.inbbbox.controler.likes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.LikedShotEntity;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LikeShotControllerApiTest {

    private static final int PAGE_COUNT = 20;
    private static final int PAGE_NUMBER = 1;
    private static final long EXAMPLE_ID = 99L;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    LikesApi likesApiMock;

    @Mock
    ShotEntity shotEntityMock;

    @Mock
    LikeShotControllerGuest guestModeController;

    @Mock
    Image imageMock;

    @Mock
    Shot shotMock;

    @InjectMocks
    LikeShotControllerApi likeShotControllerApi;

    private List<LikedShotEntity> expectedItems;

    @Before
    public void setUp() {
        LikedShotEntity entity = LikedShotEntity.builder()
                .id(EXAMPLE_ID)
                .shot(shotEntityMock)
                .createdAt("2016-12-12")
                .build();
        expectedItems = new ArrayList<>();
        expectedItems.add(entity);
        when(shotEntityMock.image()).thenReturn(imageMock);
        when(shotEntityMock.createdAt()).thenReturn(ZonedDateTime.now());
        when(likesApiMock.getLikedShots(PAGE_NUMBER, PAGE_COUNT))
                .thenReturn(Observable.just(expectedItems));
        when(shotMock.id()).thenReturn(EXAMPLE_ID);
        when(likesApiMock.isShotLiked(anyLong())).thenReturn(Completable.complete());
        when(likesApiMock.likeShot(anyLong())).thenReturn(Completable.complete());
        when(likesApiMock.unLikeShot(anyLong())).thenReturn(Completable.complete());
    }

    @Test
    public void whenGetLikedShot_thenObservableReturnsListOfLikedShots() {
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        likeShotControllerApi.getLikedShots(PAGE_NUMBER, PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void whenShotIsLikedSubscribed_thenSendIsLikedRequestToApi() {
        TestSubscriber testSubscriber = new TestSubscriber();

        likeShotControllerApi.isShotLiked(shotMock).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likesApiMock, times(1)).isShotLiked(EXAMPLE_ID);
    }

    @Test
    public void whenLikeShotSubscribed_thenSendLikeShotRequestToApi() {
        TestSubscriber testSubscriber = new TestSubscriber();

        likeShotControllerApi.likeShot(shotMock).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likesApiMock, times(1)).likeShot(EXAMPLE_ID);
    }

    @Test
    public void whenUnLikeShotSubscribed_thenSendLikeShotRequestToApi() {
        TestSubscriber testSubscriber = new TestSubscriber();

        likeShotControllerApi.unLikeShot(shotMock).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(likesApiMock, times(1)).unLikeShot(EXAMPLE_ID);
    }
}