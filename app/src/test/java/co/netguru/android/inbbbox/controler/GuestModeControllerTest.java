package co.netguru.android.inbbbox.controler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.controler.likescontroller.GuestModeController;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeControllerTest {

    private static final long EXAMPLE_SHOT_ID = 99L;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    GuestModeRepository guestModeRepositoryMock;

    @Mock
    UserController userControllerMock;

    @Mock
    Shot shotMock;

    @InjectMocks
    GuestModeController guestModeController;

    @Before
    public void setUp() {
        when(shotMock.id()).thenReturn(EXAMPLE_SHOT_ID);
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
    public void whenShotLikeTransformerRequested_thenCheckIsGuestModeIsEnabled() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getShotLikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }

    @Test
    public void whenGuestModeIsDisabledWithShotLikeAction_thenDoNotNothingWithRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        Completable.complete()
                .compose(guestModeController.getShotLikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, never()).getLikedShots();
        verify(guestModeRepositoryMock, never()).addLikedShot(any(Shot.class));
        verify(guestModeRepositoryMock, never()).isShotLiked(any(Shot.class));
        verify(guestModeRepositoryMock, never()).removeLikedShot(any(Shot.class));
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenAddLikeShotToRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getShotLikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).addLikedShot(shotMock);
    }

    @Test
    public void whenShotUnLikeTransformerRequested_thenCheckIsGuestModeIsEnabled() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getShotUnlikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }

    @Test
    public void whenGuestModeIsDisabledWithShotUnLikeAction_thenDoNotNothingWithRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        Completable.complete()
                .compose(guestModeController.getShotUnlikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, never()).getLikedShots();
        verify(guestModeRepositoryMock, never()).addLikedShot(any(Shot.class));
        verify(guestModeRepositoryMock, never()).isShotLiked(any(Shot.class));
        verify(guestModeRepositoryMock, never()).removeLikedShot(any(Shot.class));
    }

    @Test
    public void whenGuestModeIsEnabledWithLikeAction_thenRemoveLikedShotFromRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getShotUnlikeTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).removeLikedShot(shotMock);
    }

    @Test
    public void whenIsShotLikedTransformerRequested_thenCheckIsGuestModeIsEnabled() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getIsShotLikedTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }

    @Test
    public void whenGuestModeIsDisabledWithIsShotLikedAction_thenDoNotNothingWithRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        Completable.complete()
                .compose(guestModeController.getIsShotLikedTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, never()).getLikedShots();
        verify(guestModeRepositoryMock, never()).addLikedShot(any(Shot.class));
        verify(guestModeRepositoryMock, never()).isShotLiked(any(Shot.class));
        verify(guestModeRepositoryMock, never()).removeLikedShot(any(Shot.class));
    }

    @Test
    public void whenGuestModeIsEnabledWithIsShotLikedAction_thenCallIsShotLikedActionInRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Completable.complete()
                .compose(guestModeController.getIsShotLikedTransformer(shotMock))
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).isShotLiked(shotMock);
    }

    @Test
    public void whenGetCachedShotsTransformerRequested_thenCheckIsGuestModeIsEnabled() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Observable
                .just(Statics.SHOT_LIST)
                .compose(guestModeController.getGuestModeCachedShotTransformer())
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }

    @Test
    public void whenGuestModeIsDisabledWithGetCachedShotAction_thenDoNotNothingWithRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        Observable
                .just(Statics.SHOT_LIST)
                .compose(guestModeController.getGuestModeCachedShotTransformer())
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, never()).getLikedShots();
        verify(guestModeRepositoryMock, never()).addLikedShot(any(Shot.class));
        verify(guestModeRepositoryMock, never()).isShotLiked(any(Shot.class));
        verify(guestModeRepositoryMock, never()).removeLikedShot(any(Shot.class));
    }

    @Test
    public void whenGuestModeIsEnabledWithGetCachedShotAction_thenCallGetLikedShotsActionInRepository() {
        TestSubscriber testSubscriber = new TestSubscriber();
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        Observable
                .just(Statics.SHOT_LIST)
                .compose(guestModeController.getGuestModeCachedShotTransformer())
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(guestModeRepositoryMock, times(1)).getLikedShots();
    }
}