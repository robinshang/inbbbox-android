package co.netguru.android.inbbbox.data.like.controllers;

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
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;
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
    GuestModeLikesRepository guestModeLikesRepository;

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
        when(guestModeLikesRepository.addLikedShot(any(Shot.class)))
                .thenReturn(Completable.complete());
        when(guestModeLikesRepository.getLikedShots())
                .thenReturn(Observable.just(Statics.SHOT_LIST));
        when(guestModeLikesRepository.removeLikedShot(any(Shot.class)))
                .thenReturn(Completable.complete());
        when(guestModeLikesRepository.isShotLiked(any(Shot.class)))
                .thenReturn(Single.just(true));
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenAddLikeShotToRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();


        guestModeController.likeShot(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        verify(guestModeLikesRepository, times(1)).addLikedShot(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenRemoveLikedShotFromRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();

        guestModeController.unLikeShot(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        verify(guestModeLikesRepository, times(1)).removeLikedShot(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithIsShotLikedAction_thenCallIsShotLikedActionInRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();

        guestModeController.isShotLiked(shotMock)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        verify(guestModeLikesRepository, times(1)).isShotLiked(shotMock);
    }

    @Test
    public void whenGuestModeIsEnabledWithGetCachedShotAction_thenCallGetLikedShotsActionInRepository() {
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        guestModeController.getLikedShots(5, 1).
                subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        verify(guestModeLikesRepository, times(1)).getLikedShots();
    }
}