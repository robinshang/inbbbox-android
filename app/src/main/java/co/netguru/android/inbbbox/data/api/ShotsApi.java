package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.Shot;
import co.netguru.android.inbbbox.utils.Constants.API;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ShotsApi {

    @GET
    Observable<List<Shot>> getShots(
            @Query(API.SHOT_KEY_LIST) String shots,
            @Query(API.SHOTS_KEY_TIME_FRAME) String timeFrame,
            @Query(API.SHOTS_KEY_SORT) String sort);
}
