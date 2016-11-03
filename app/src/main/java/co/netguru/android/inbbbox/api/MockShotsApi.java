package co.netguru.android.inbbbox.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.netguru.android.inbbbox.data.models.Image;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.utils.Constants;
import retrofit2.http.Query;
import rx.Observable;

public class MockShotsApi implements ShotsApi {

    public static int ITEM_COUNT = new Random().nextInt(1000);

    public MockShotsApi() {
    }

    @Override
    public Observable<List<ShotEntity>> getFilteredShots(@Query(Constants.API.SHOTS_KEY_LIST) String list,
                                                         @Query(Constants.API.SHOTS_KEY_TIME_FRAME) String timeFrame,
                                                         @Query(Constants.API.SHOTS_KEY_DATE) String date,
                                                         @Query(Constants.API.SHOTS_KEY_SORT) String sort) {
        return Observable.just(getFilteredMockedData());
    }

    @Override
    public Observable<List<ShotEntity>> getFollowingShots() {
        return Observable.just(getFollowingMockedData());
    }

    private static List<ShotEntity> getFollowingMock(int count, String label) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            ShotEntity entity = new ShotEntity();
            entity.setTitle(label + i);
            entity.setImage(image);
            result.add(entity);
        }
        return result;
    }

    public static List<ShotEntity> getFollowingMockedData() {
        return getFollowingMock(ITEM_COUNT, "following");
    }

    public static List<ShotEntity> getFilteredMockedData() {
        return getFollowingMock(ITEM_COUNT, "filtered");
    }

}
