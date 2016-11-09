package co.netguru.android.inbbbox.api;


import java.util.List;

import co.netguru.android.inbbbox.model.api.BucketShot;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

public interface BucketApi {

    @GET("buckets/{id}/shot")
    Single<List<BucketShot>> getBucketShots(@Path("id") long id);

}