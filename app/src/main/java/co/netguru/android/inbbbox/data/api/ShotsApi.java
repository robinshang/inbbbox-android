package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.utils.Constants.API;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ShotsApi {

    @GET("shots")
    Observable<List<ShotEntity>> getFilteredShots(@Query(API.SHOTS_KEY_LIST) String list,
                                                  @Query(API.SHOTS_KEY_TIME_FRAME) String timeFrame,
                                                  @Query(API.SHOTS_KEY_DATE) String date,
                                                  @Query(API.SHOTS_KEY_SORT) String sort);

    @GET("user/following/shots")
    Observable<List<ShotEntity>> getFollowingShots();

    @GET("users/{user}/shots")
    Observable<List<ShotEntity>> getFollowedUserShots(@Path("user") int userId);
}
