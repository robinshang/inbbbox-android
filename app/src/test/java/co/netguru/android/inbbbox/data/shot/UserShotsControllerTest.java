package co.netguru.android.inbbbox.data.shot;

import org.junit.Assert;
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
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserShotsControllerTest {

    private static final int TEST_ITEMS_COUNT = 200;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotsApi shotsApi;

    @InjectMocks
    UserShotsController shotsController;

    private List<ShotEntity> inputList;

    @Before
    public void setUp() {
        inputList = Statics.getShotsEntityList(TEST_ITEMS_COUNT);
        when(shotsApi.getUserShots(anyLong(), anyInt(), anyInt(), anyString()))
                .thenReturn(Observable.just(inputList));
    }

    @Test
    public void whenGetUserShotsSubscribed_thenReturnShotsList() {
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getUserShotsList(99, 1, 1).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        for (int i = 0; i < TEST_ITEMS_COUNT; i++) {
            Assert.assertEquals(inputList.get(i).id(), resultList.get(i).id());
        }
    }
}