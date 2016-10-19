package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.utils.Constants.API;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ShotsApi {

    @GET("/shots")
    Observable<List<ShotEntity>> getFilteredShots();

    @GET("/user/following/shots")
    Observable<List<ShotEntity>> getFollowingShots();
}
