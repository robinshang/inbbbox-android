package co.netguru.android.inbbbox.controller;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static co.netguru.android.inbbbox.Statics.ITEM_COUNT;
import static co.netguru.android.inbbbox.Statics.getFilteredMockedData;
import static co.netguru.android.inbbbox.Statics.getFollowingMockedData;
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

    @InjectMocks
    private ShotsController shotsController;

    @Before
    public void setUp() {
        when(settingsControllerMock.getStreamSourceSettings()).thenReturn(Single.just(streamSourceSettings));
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
    @Ignore
    @Test
    public void whenFollowingSourceEnabled_thenCallGetFollowingShots() {
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFollowingShots(SHOT_PAGE, SHOT_PAGE_COUNT);
    }

    @Ignore
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

    @Ignore
    @Test
    public void whenFollowingSourceEnabled_thenReturnFollowingViewItems() {
        List<ShotEntity> listOfExpected = getFollowingMockedData();
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        for (int i = 0; i < ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                                        .title()
                                        .equals(listOfExpected.get(i).title()), true);
        }
    }

    @Ignore
    @Test
    public void whenNewTodayEnabled_thenCallGetShotsByDate() {
        setupStreamSourceStates(false, true, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Ignore
    @Test
    public void whenPopularTodayEnabled_thenCallGetShotsByDate() {
        setupStreamSourceStates(false, false, true, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Ignore
    @Test
    public void whenDebutTodayEnabled_thenCallGetShotsByList() {
        setupStreamSourceStates(false, false, false, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByList(anyString(), anyInt(), anyInt());
    }

    @Ignore
    @Test
    public void whenAllFilteredEnabled_thenCallGetShotsByDateTwoTimes() {
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(2)).getShotsByDateSort(anyString(), anyString(), anyInt(), anyInt());
    }

    @Ignore
    @Test
    public void whenAllFilteredEnabled_thenCallGetShotsByList() {
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getShotsByList(anyString(), anyInt(), anyInt());
    }

    @Ignore
    @Test
    public void whenNewTodayOfFilteredSourceEnabled_thenReturnFilteredViewItems() {
        when(shotsApiMock.getFollowingShots(SHOT_PAGE, SHOT_PAGE_COUNT)).thenReturn(Observable.just(getFilteredMockedData()));
        List<ShotEntity> listOfExpected = getFilteredMockedData();
        setupStreamSourceStates(false, true, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        for (int i = 0; i < ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                                        .title()
                                        .equals(listOfExpected.get(i).title()), true);
        }
    }

    @Ignore
    @Test
    public void whenAllOfFilteredSourceEnabled_thenReturnFilteredViewItemsFromThreeSources() {
        List<ShotEntity> listOfExpected = getFilteredMockedData();
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        for (int i = 0; i < ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                                        .title()
                                        .equals(listOfExpected.get(i).title()), true);
        }
    }


    @Ignore
    @Test
    public void whenAllSourcesEnabled_thenReturnFilteredAndFollowingViewItemsFrom() {
        List<ShotEntity> expectedItems = new ArrayList<>();
        expectedItems.addAll(getFilteredMockedData());
        expectedItems.addAll(getFollowingMockedData());
        List<String> expetedTitltes = new ArrayList<>();
        for (ShotEntity entity : expectedItems) {
            expetedTitltes.add(entity.title());
        }

        setupStreamSourceStates(true, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots(SHOT_PAGE, SHOT_PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        for (int i = 0; i < ITEM_COUNT; i++) {
            Assert.assertEquals(expetedTitltes.contains(resultList.get(i).title()), true);
        }
    }

    @Ignore
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