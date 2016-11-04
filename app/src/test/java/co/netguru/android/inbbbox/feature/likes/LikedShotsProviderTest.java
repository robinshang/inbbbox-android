package co.netguru.android.inbbbox.feature.likes;

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

import co.netguru.android.inbbbox.data.api.LikesApi;
import co.netguru.android.inbbbox.models.Image;
import co.netguru.android.inbbbox.models.LikedShotEntity;
import co.netguru.android.inbbbox.models.ShotEntity;
import co.netguru.android.inbbbox.models.ui.LikedShot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LikedShotsProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    LikesApi likesApiMock;

    @Mock
    LikedShotsMapper likedShotsMapperMock;

    @Mock
    ShotEntity shotEntityMock;

    @Mock
    Image imageMock;

    @InjectMocks
    LikedShotsProvider likedShotsProvider;

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
        when(shotEntityMock.getImage()).thenReturn(imageMock);
        when(likesApiMock.getLikedShots()).thenReturn(Observable.just(expectedItems));
        when(likedShotsMapperMock.toLikedShot(entity)).thenCallRealMethod();
    }

    @Test
    public void whenGetLikedShot_thenObservableReturnsListOfLikedShots() {
        TestSubscriber<LikedShot> testSubscriber = new TestSubscriber<>();

        likedShotsProvider.getLikedShots().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

}