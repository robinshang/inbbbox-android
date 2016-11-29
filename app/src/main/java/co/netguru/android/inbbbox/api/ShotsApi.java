package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.Constants.API;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.CommentEntity;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface ShotsApi {

    @GET("shots")
    Observable<List<ShotEntity>> getShotsByList(@Query(API.SHOTS_KEY_LIST) String list,
                                                @Query("page") int pageNumber, @Query("per_page") int pageCount);

    @GET("shots")
    Observable<List<ShotEntity>> getShotsByDateSort(@Query(API.SHOTS_KEY_DATE) String date,
                                                    @Query(API.SHOTS_KEY_SORT) String sort,
                                                    @Query("page") int pageNumber, @Query("per_page") int pageCount);

    @GET("user/following/shots")
    Observable<List<ShotEntity>> getFollowingShots(@Query("page") int pageNumber, @Query("per_page") int pageCount);

    @GET("users/{user}/shots")
    Observable<List<ShotEntity>> getUserShots(@Path("user") long userId,
                                              @Query("page") int pageNumber, @Query("per_page") int pageCount);

    @GET("shots/{shotId}/comments")
    Observable<List<CommentEntity>> getShotComments(@Path("shotId") String shotId);

    @GET("shots/{shotId}/buckets")
    Observable<List<Bucket>> getBucketsList(@Path("shotId") String shotId);

    @POST("shots/{shotId}/comments")
    Single<CommentEntity> createComment(@Path("shotId") String shotId, @Body String comment);
}
