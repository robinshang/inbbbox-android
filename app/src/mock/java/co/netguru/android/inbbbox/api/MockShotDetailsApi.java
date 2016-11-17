package co.netguru.android.inbbbox.api;

import co.netguru.android.inbbbox.model.api.ShotEntity;
import retrofit2.http.Path;
import rx.Observable;

public class MockShotDetailsApi implements ShotDetailsApi {
    @Override
    public Observable<ShotEntity> getShot(@Path("id") String id) {
        return Observable.just(new ShotEntity());
    }
}
