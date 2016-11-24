package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.UserEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface UserApi {

    @GET("user")
    Observable<UserEntity> getAuthenticatedUser();

    @GET("user/buckets")
    Single<List<Bucket>> getUserBucketsList(@Query("page") int pageNumber, @Query("per_page") int pageCount);
}
