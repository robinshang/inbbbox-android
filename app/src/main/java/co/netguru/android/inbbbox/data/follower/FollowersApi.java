package co.netguru.android.inbbbox.data.follower;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FollowersApi {

    @GET("user/following")
    Observable<List<FollowerEntity>> getFollowedUsers(@Query("page") int pageNumber, @Query("per_page") int pageCount);

    @DELETE("users/{user}/follow")
    Completable unFollowUser(@Path("user") long id);

    @PUT("users/{user}/follow")
    Completable followUser(@Path("user") long id);

    @GET("user/following/{user}")
    Single<Response<Void>> checkIfUserIsFollowed(@Path("user") long id);
}
