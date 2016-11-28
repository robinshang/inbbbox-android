package co.netguru.android.inbbbox.api;


import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Field;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Single;

public class MockedBucketApi implements BucketApi {

    private static final List<ShotEntity> mockedBucketShots = getMockShots();

    @Override
    public Single<List<ShotEntity>> getBucketShots(@Path("id") long id, @Query("page") int pageNumber, @Query("per_page") int pageCount) {
        return Single.fromCallable(() -> new ArrayList<>(mockedBucketShots));
    }

    @Override
    public Completable addShotToBucket(@Path("id") long bucketId, @Field("shot_id") long shotId) {
        return Completable.complete();
    }

    private static List<ShotEntity> getMockShots() {
        ArrayList<ShotEntity> shots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Image image = Image.builder().build();
            ShotEntity entity = ShotEntity.builder()
                    .id(i)
                    .title("test")
                    .image(image)
                    .createdAt(LocalDateTime.now())
                    .animated(false)
                    .likesCount(2)
                    .bucketsCount(3)
                    .createdAt(LocalDateTime.now())
                    .build();
            mockedBucketShots.add(entity);
        }
        return shots;
    }
}