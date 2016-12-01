package co.netguru.android.inbbbox.api;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.CommentEntity;
import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class MockShotsApi implements ShotsApi {

    public static final int ITEM_COUNT = new Random().nextInt(1000);

    @Override
    public Observable<List<ShotEntity>> getShotsByList(@Query(Constants.API.SHOTS_KEY_LIST) String list,
                                                       @Query("page") int pageNumber,
                                                       @Query("per_page") int pageCount) {
        return Observable.just(getFilteredMockedData());
    }

    @Override
    public Observable<List<ShotEntity>> getShotsByDateSort(@Query(Constants.API.SHOTS_KEY_DATE) String date,
                                                           @Query(Constants.API.SHOTS_KEY_SORT) String sort,
                                                           @Query("page") int pageNumber, @Query("per_page") int pageCount) {
        return Observable.just(getFilteredMockedData());
    }

    @Override
    public Observable<List<ShotEntity>> getFollowingShots(@Query("page") int pageNumber,
                                                          @Query("per_page") int pageCount) {
        return Observable.just(getFollowingMockedData());
    }

    @Override
    public Observable<List<ShotEntity>> getUserShots(@Path("user") long userId, @Query("page") int pageNumber,
                                                     @Query("per_page") int pageCount) {
        return Observable.just(getFollowingMockedData());
    }

    @Override
    public Observable<List<CommentEntity>> getShotComments(@Path("shotId") String shotId, int pageNumber, int commentsPerPage) {
        return Observable.just(Collections.emptyList());
    }

    @Override
    public Observable<List<Bucket>> getBucketsList(String shotId) {
        return Observable.just(Collections.emptyList());
    }

    @Override
    public Single<CommentEntity> createComment(@Path("shotId") String shotId, @Body String comment) {
        return Single.just(new CommentEntity());
    }

    @Override
    public Completable deleteComment(@Path("shotId") String shotId, @Path("commentId") String commentId) {
        return Completable.complete();
    }

    private static List<ShotEntity> getFollowingMock(int count, String label) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            result.add(
                    ShotEntity.builder()
                            .id(i)
                            .title(label + i)
                            .image(image)
                            .createdAt(LocalDateTime.now())
                            .animated(false)
                            .likesCount(2)
                            .bucketsCount(3)
                            .createdAt(LocalDateTime.now())
                            .commentsCount(2)
                            .build());
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
