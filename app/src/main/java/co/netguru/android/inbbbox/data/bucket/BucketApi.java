package co.netguru.android.inbbbox.data.bucket;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Single;

public interface BucketApi {

    @GET("buckets/{id}/shots")
    Single<List<ShotEntity>> getBucketShotsList(@Path("id") long id, @Query("page") int pageNumber, @Query("per_page") int pageCount);

    @FormUrlEncoded
    @PUT("buckets/{id}/shots")
    Completable addShotToBucket(@Path("id") long bucketId, @Field("shot_id") long shotId);

    @FormUrlEncoded
    @POST("buckets/")
    Single<Bucket> createBucket(@Field("name") @NonNull String newBucketName,
                                @Field("description") @Nullable String bucketDescription);

    @DELETE("buckets/{id}")
    Completable deleteBucket(@Path("id") long bucketId);

    @GET("shots/{shotId}/buckets")
    Single<List<Bucket>> getShotBucketsList(@Path("shotId") long shotId);
}