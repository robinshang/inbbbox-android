package co.netguru.android.inbbbox.controler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeControllerTest {

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
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));
    }

    @Test
    public void whenShotLikeTransformerRequested_thenCheckIsGuestModeIsEnabled() {
        TestSubscriber testSubscriber = new TestSubscriber();

        Completable.complete().compose(guestModeController.getShotLikeTransformer(shotMock));

        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }
}