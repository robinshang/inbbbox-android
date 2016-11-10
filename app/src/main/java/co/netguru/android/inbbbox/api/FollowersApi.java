package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import retrofit2.http.GET;
import rx.Observable;

public interface FollowersApi {

    @GET("user/following")
    Observable<List<FollowerEntity>> getFollowedUsers();
}
