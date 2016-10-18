package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.viewmodels.Shot;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotsProvider {

    private ShotsApi shotsApi;
    private ShotsMapper mapper;

    @Inject
    ShotsProvider(ShotsApi shotsApi, ShotsMapper mapper) {
        this.shotsApi = shotsApi;
        this.mapper = mapper;
    }

    public Observable<List<Shot>> getShots() {
        return shotsApi.getShots("", "", "")
                .compose(fromListObservable())
                .map(entity -> mapper.getShot(entity))
                .toList();
    }
}
