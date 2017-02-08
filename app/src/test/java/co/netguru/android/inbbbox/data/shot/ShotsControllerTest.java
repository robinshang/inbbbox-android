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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsControllerTest {

    private static final int SHOT_PAGE_COUNT = 15;
    private static final int SHOT_PAGE = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public SettingsController settingsControllerMock;

    @Mock
    private StreamSourceSettings streamSourceSettings;

    @Mock
    public ShotsApi shotsApiMock;

    @Mock
    public ShotEntity shotEntityMock;

    @InjectMocks
    private ShotsController shotsController;

    @Mock
    private List<ShotEntity> shotEntityList;

    @Before
    public void setUp() {
        Random random = new Random();
        for (int i = 1; i < random.nextInt(200); i++) {
            shotEntityList.add(shotEntityMock);
        }

        shotEntityList = new ArrayList<>();

        when(settingsControllerMock.getStreamSourceSettings())
                .thenReturn(Single.just(streamSourceSettings));
        when(shotsApiMock.getFollowingShots(anyInt(), anyInt()))
                .thenReturn(Observable.just(shotEntityList));
        when(shotsApiMock.getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Observable.just(shotEntityList));
        when(shotsApiMock.getShotsByList(anyString(), anyInt(), anyInt()))
                .thenReturn(Observable.just(shotEntityList));
    }

    private void setupStreamSourceStates(boolean following,
                                         boolean newToday,
                                         boolean popularToday,
                                         boolean debut) {

        when(streamSourceSettings.isFollowing()).thenReturn(following);
        when(streamSourceSettings.isNewToday()).thenReturn(newToday);
        when(streamSourceSettings.isPopularToday()).thenReturn(popularToday);
        when(streamSourceSettings.isDebut()).thenReturn(debut);
    }

    //Following shots
    @Test
    public void whenFollowingSourceEnableSubscribed_thenGetStreamSourceSettings() {
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(settingsControllerMock).getStreamSourceSettings();
    }

    @Test
    public void whenFollowingSourceEnabled_thenCallGetFollowingShots() {
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock).getFollowingShots(SHOT_PAGE, SHOT_PAGE_COUNT);
    }

    @Test
    public void whenFilteredSourceDisabled_thenFilteredNotCalled() {
        setupStreamSourceStates(true, false, false, false);
        when(shotsApiMock.getFollowingShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.empty());
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, never()).getShotsByDateSort(anyString(),
                anyString(), anyInt(), anyInt());
        verify(shotsApiMock, never()).getShotsByList(anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenNewTodayEnabled_thenCallGetShotsByDate() {
        setupStreamSourceStates(false, true, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenPopularTodayEnabled_thenCallGetShotsByDate() {
        setupStreamSourceStates(false, false, true, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenDebutTodayEnabled_thenCallGetShotsByList() {
        setupStreamSourceStates(false, false, false, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByList(anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenAllFilteredEnabled_thenCallGetShotsByDateTwoTimes() {
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(2)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenAllFilteredEnabled_thenCallGetShotsByList() {
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByList(anyString(), anyInt(), anyInt());
    }

    @Test
    public void whenNoSourcesEnabled_thenReturnEmptyResult() {
        setupStreamSourceStates(false, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        Assert.assertEquals(resultList.isEmpty(), true);
    }
}