package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.FollowerEntity;
import retrofit2.http.GET;
import rx.Observable;

public interface FollowersApi {

    @GET("user/following")
    Observable<List<FollowerEntity>> getFollowedUsers();
}
