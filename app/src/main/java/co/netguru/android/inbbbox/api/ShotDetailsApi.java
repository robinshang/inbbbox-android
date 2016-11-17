package co.netguru.android.inbbbox.api;

import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

@FunctionalInterface
public interface ShotDetailsApi {

    @GET("shots/{id}")
    Observable<ShotEntity> getShot(@Path("id") String id);
}
