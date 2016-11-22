package co.netguru.android.inbbbox.api;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class MockShotsApi implements ShotsApi {

    public static final int ITEM_COUNT = new Random().nextInt(1000);

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

    @Override
    public Observable<List<ShotEntity>> getUserShots(@Path("user") long userId, @Query("page") int pageNumber,
                                                     @Query("per_page") int pageCount) {
        return Observable.just(getFollowingMockedData());
    }

    private static List<ShotEntity> getFollowingMock(int count, String label) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            ShotEntity entity = new ShotEntity();
            entity.setId(i);
            entity.setTitle(label + i);
            entity.setImage(image);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setAnimated(false);
            entity.setLikesCount(2);
            entity.setBucketsCount(3);
            entity.setCreatedAt(LocalDateTime.now());
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
