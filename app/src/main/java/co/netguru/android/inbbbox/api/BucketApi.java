package co.netguru.android.inbbbox.api;


import java.util.List;

import co.netguru.android.inbbbox.model.api.BucketShot;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Completable;
import rx.Single;

public interface BucketApi {

    @GET("buckets/{id}/shot")
    Single<List<BucketShot>> getBucketShots(@Path("id") long id);

    @FormUrlEncoded
    @PUT("buckets/{id}/shots")
    Completable addShotToBucket(@Path("id") long bucketId, @Field("shot_id") long shotId);
}