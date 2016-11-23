package co.netguru.android.inbbbox.api;


import java.util.List;

import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Single;

public interface BucketApi {

    @GET("buckets/{id}/shots")
    Single<List<ShotEntity>> getBucketShots(@Path("id") long id, @Query("page") int pageNumber, @Query("per_page") int pageCount);

    @FormUrlEncoded
    @PUT("buckets/{id}/shots")
    Completable addShotToBucket(@Path("id") long bucketId, @Field("shot_id") long shotId);
}