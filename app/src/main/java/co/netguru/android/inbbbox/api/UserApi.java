package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.User;
import retrofit2.http.GET;
import rx.Observable;
import rx.Single;

public interface UserApi {

    @GET("user")
    Observable<User> getAuthenticatedUser();

    @GET("user/buckets")
    Single<List<Bucket>> getUserBucketsList();
}
