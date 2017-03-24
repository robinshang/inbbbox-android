package co.netguru.android.inbbbox.data.dribbbleuser.user;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

import static co.netguru.android.inbbbox.data.cache.CacheStrategy.HEADER_CACHE_CONTROL;

public interface UserApi {

    @GET("user")
    Observable<UserEntity> getAuthenticatedUser();

    @GET("user/buckets")
    Single<List<Bucket>> getAuthenticatedUserBucketsList(@Query("page") int pageNumber,
                                                         @Query("per_page") int pageCount);

    @GET("users/{id}/buckets")
    Single<List<Bucket>> getUserBucketsList(@Path("id") long userId, @Query("page") int pageNumber,
                                            @Query("per_page") int pageCount);
}
