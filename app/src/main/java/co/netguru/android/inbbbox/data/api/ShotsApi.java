package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.inbbbox.utils.Constants.API;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ShotsApi {

    @GET(Constants.API.SHOTS_ENDPOINT)
    Observable<List<ShotEntity>> getFilteredShots(@Query(API.SHOTS_KEY_LIST) String list,
                                                  @Query(API.SHOTS_KEY_TIME_FRAME) String timeFrame,
                                                  @Query(API.SHOTS_KEY_DATE) String date,
                                                  @Query(API.SHOTS_KEY_SORT) String sort);

    @GET(Constants.API.FOLLOWING_SHOTS_ENDPOINT)
    Observable<List<ShotEntity>> getFollowingShots();
}
