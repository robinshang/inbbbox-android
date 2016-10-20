package co.netguru.android.inbbbox.feature.shots;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.api.MockShotsApi;
import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.models.StreamSourceState;
import co.netguru.android.inbbbox.data.viewmodels.Shot;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public ShotsMapper shotsMapperMock;

    @Mock
    public ShotsRequestFactory shotsRequestFactoryMock;

    @Mock
    public DataSource<Settings> dataSourceMock;

    @Mock
    private Settings settingsMock;

    @Mock
    private StreamSourceState streamSourceState;

    @InjectMocks
    private ShotsProvider shotsProvider;

    @Spy
    public ShotsApi shotsApiMock = new MockShotsApi(3);

    private List<ShotEntity> followingEntity = new ArrayList<>();

    @Before
    public void setUp() {
        when(settingsMock.getStreamSourceState()).thenReturn(streamSourceState);
        when(dataSourceMock.get()).thenReturn(Observable.just(settingsMock));

    }

    private void setupStreamSourceStates(boolean following,
                                         boolean newToday,
                                         boolean popularToday,
                                         boolean debut) {
        when(streamSourceState.getFollowingState()).thenReturn(following);
        when(streamSourceState.getNewTodayState()).thenReturn(newToday);
        when(streamSourceState.getPopularTodayState()).thenReturn(popularToday);
        when(streamSourceState.getDebut()).thenReturn(debut);
    }

    //Following shots
    @Test
    public void whenFollowingSourceEnabled_thenCallGetFollowingShots() {
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber testSubscriber = new TestSubscriber();

        shotsProvider.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFollowingShots();
    }

    @Test
    public void whenFilteredSourceDisabled_thenFilteredNotCalled() {
        setupStreamSourceStates(true, false, false, false);
        when(shotsApiMock.getFollowingShots()).thenReturn(Observable.empty());
        TestSubscriber testSubscriber = new TestSubscriber();

        shotsProvider.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, never()).getFilteredShots(anyObject());
    }

    @Test
    public void whenFollowingSourceEnabled_thenReturnFollowingViewItems() {
        List<ShotEntity> listOfExpected = ((MockShotsApi) shotsApiMock).getMockedData();
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber();

        shotsProvider.getShots().subscribe(testSubscriber);

        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(resultList.get(0).getTitle().equals(listOfExpected.get(0).getTitle()), true);
        Assert.assertEquals(resultList.get(1).getTitle().equals(listOfExpected.get(1).getTitle()), true);
        Assert.assertEquals(resultList.get(2).getTitle().equals(listOfExpected.get(2).getTitle()), true);
    }
}