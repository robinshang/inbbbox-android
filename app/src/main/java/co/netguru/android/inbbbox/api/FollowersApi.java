package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FollowersApi {

    @GET("user/following")
    Observable<List<FollowerEntity>> getFollowedUsers(@Query("page") int pageNumber, @Query("per_page") int pageCount);
}
