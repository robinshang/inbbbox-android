package co.netguru.android.inbbbox.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.LikedShotEntity;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LikedShotsControllerTest {

    private static final int PAGE_COUNT = 20;
    private static final int PAGE_NUMBER = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    LikesApi likesApiMock;

    @Mock
    ShotEntity shotEntityMock;

    @Mock
    Image imageMock;

    @InjectMocks
    LikedShotsController likedShotsController;

    private List<LikedShotEntity> expectedItems;

    @Before
    public void setUp() {
        LikedShotEntity entity = LikedShotEntity.builder()
                .id(99)
                .shot(shotEntityMock)
                .createdAt("2016-12-12")
                .build();
        expectedItems = new ArrayList<>();
        expectedItems.add(entity);
        when(shotEntityMock.image()).thenReturn(imageMock);
        when(shotEntityMock.createdAt()).thenReturn(LocalDateTime.now());
        when(likesApiMock.getLikedShots(PAGE_NUMBER, PAGE_COUNT)).thenReturn(Observable.just(expectedItems));
    }

    @Test
    public void whenGetLikedShot_thenObservableReturnsListOfLikedShots() {
        TestSubscriber<List<Shot>> testSubscriber = new TestSubscriber<>();

        likedShotsController.getLikedShots(PAGE_NUMBER, PAGE_COUNT).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

}