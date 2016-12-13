package co.netguru.android.inbbbox.controler.likescontroller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LikeShotControllerGuestTest {

    private static final long EXAMPLE_SHOT_ID = 99L;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    GuestModeRepository guestModeRepositoryMock;

    @Mock
    Shot shotMock;

    @Mock
    LikesApi likesApiMock;

    @InjectMocks
    LikeShotControllerGuest guestModeController;

    @Before
    public void setUp() {
        when(shotMock.id()).thenReturn(EXAMPLE_SHOT_ID);
        when(likesApiMock.getLikedShots(anyInt(), anyInt())).thenReturn(Observable.empty());

        when(guestModeRepositoryMock.addLikedShot(any(Shot.class)))
                .thenReturn(Completable.complete());
        when(guestModeRepositoryMock.getLikedShots())
                .thenReturn(Observable.just(Statics.SHOT_LIST));
        when(guestModeRepositoryMock.removeLikedShot(any(Shot.class)))
                .thenReturn(Completable.complete());
        when(guestModeRepositoryMock.isShotLiked(any(Shot.class)))
                .thenReturn(Completable.complete());
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenAddLikeShotToRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();


        guestModeController.likeShot(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).addLikedShot(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenRemoveLikedShotFromRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();

        guestModeController.unLikeShot(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).removeLikedShot(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithIsShotLikedAction_thenCallIsShotLikedActionInRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();

        guestModeController.isShotLiked(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).isShotLiked(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithGetCachedShotAction_thenCallGetLikedShotsActionInRepository() {
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        guestModeController.getLikedShots(5, 1).
                subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).getLikedShots();
    }
}