package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import retrofit2.http.GET;
import rx.Observable;

public interface LikesApi {

    @GET("/user/likes")
    Observable<List<ShotEntity>> getLikedShots();
}
