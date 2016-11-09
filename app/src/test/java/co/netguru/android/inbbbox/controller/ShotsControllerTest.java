package co.netguru.android.inbbbox.controller;

import android.media.Image;

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

import co.netguru.android.inbbbox.api.MockShotsApi;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.controler.SettingsController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.api.FilteredShotsParams;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.localrepository.Settings;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public SettingsController settingsControllerMock;

    @Mock
    public Image imageMock;

    @Mock
    private Settings settingsMock;

    @Mock
    private StreamSourceSettings streamSourceSettings;

    @Spy
    public ShotsApi shotsApiMock = new MockShotsApi();

    @Spy
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
    @Test
    public void whenFollowingSourceEnabled_thenCallGetFollowingShots() {
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFollowingShots();
    }

    @Test
    public void whenFilteredSourceDisabled_thenFilteredNotCalled() {
        setupStreamSourceStates(true, false, false, false);
        when(shotsApiMock.getFollowingShots()).thenReturn(Observable.empty());
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, never()).getFilteredShots(anyString(),
                anyString(),
                anyString(),
                anyString());
    }

    @Test
    public void whenFollowingSourceEnabled_thenReturnFollowingViewItems() {
        List<ShotEntity> listOfExpected = MockShotsApi.getFollowingMockedData();
        setupStreamSourceStates(true, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        for (int i = 0; i < MockShotsApi.ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                    .title()
                    .equals(listOfExpected.get(i).getTitle()), true);
        }
    }

    @Test
    public void whenNewTodayEnabled_thenCallGetFilteredShots() {
        setupStreamSourceStates(false, true, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }

    @Test
    public void whenPopularTodayEnabled_thenCallGetFilteredShots() {
        setupStreamSourceStates(false, false, true, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }

    @Test
    public void whenDebutTodayEnabled_thenCallGetFilteredShots() {
        setupStreamSourceStates(false, false, false, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(1)).getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }

    @Test
    public void whenAllFilteredEnabled_thenCallGetFilteredShotsThreeTimes() {
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(shotsApiMock, times(3)).getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }

    @Test
    public void whenNewTodayOfFilteredSourceEnabled_thenReturnFilteredViewItems() {
        List<ShotEntity> listOfExpected = MockShotsApi.getFilteredMockedData();
        setupStreamSourceStates(false, true, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);
        for (int i = 0; i < MockShotsApi.ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                    .title()
                    .equals(listOfExpected.get(i).getTitle()), true);
        }
    }

    @Test
    public void whenAllOfFilteredSourceEnabled_thenReturnFilteredViewItemsFromThreeSources() {
        List<ShotEntity> listOfExpected = MockShotsApi.getFilteredMockedData();
        setupStreamSourceStates(false, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        for (int i = 0; i < MockShotsApi.ITEM_COUNT; i++) {
            Assert.assertEquals(resultList.get(i)
                    .title()
                    .equals(listOfExpected.get(i).getTitle()), true);
        }
    }

    @Test
    public void whenAllSourcesEnabled_thenReturnFilteredAndFollowingViewItemsFrom() {
        List<ShotEntity> expectedItems = new ArrayList<>();
        expectedItems.addAll(MockShotsApi.getFilteredMockedData());
        expectedItems.addAll(MockShotsApi.getFollowingMockedData());
        List<String> expetedTitltes = new ArrayList<>();
        for (ShotEntity entity : expectedItems) {
            expetedTitltes.add(entity.getTitle());
        }

        setupStreamSourceStates(true, true, true, true);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        for (int i = 0; i < MockShotsApi.ITEM_COUNT; i++) {
            Assert.assertEquals(expetedTitltes.contains(resultList.get(i).title()), true);
        }
    }

    @Test
    public void whenNoSourcesEnabled_thenReturnEmptyResult() {
        setupStreamSourceStates(false, false, false, false);
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();
        FilteredShotsParams params = FilteredShotsParams.newBuilder()
                .date("testDate")
                .timeFrame("testParam")
                .sort("testSort")
                .list("testList")
                .build();
        doReturn(params).when(shotsController).getShotsParams(streamSourceSettings);

        shotsController.getShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<Shot> resultList = testSubscriber.getOnNextEvents().get(0);

        Assert.assertEquals(resultList.isEmpty(), true);
    }
}