package co.netguru.android.inbbbox.data.follower;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public interface FollowersApi {

    @GET("user/following")
    Observable<List<FollowerEntity>> getFollowedUsers(@Query("page") int pageNumber, @Query("per_page") int pageCount);

    @DELETE("users/{user}/follow")
    Completable unFollowUser(@Path("user") long id);
}
