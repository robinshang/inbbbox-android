package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.LikedShotEntity;
import retrofit2.http.GET;
import rx.Observable;

public interface LikesApi {

    @GET("user/likes")
    Observable<List<LikedShotEntity>> getLikedShots();
}
