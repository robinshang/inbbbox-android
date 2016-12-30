package co.netguru.android.inbbbox.data.dribbbleuser.user;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
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
