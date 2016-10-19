package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.viewmodels.Shot;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotsProvider {

    private ShotsApi shotsApi;
    private ShotsMapper mapper;
    private CacheEndpoint<Settings> cacheEndpoint;

    @Inject
    ShotsProvider(ShotsApi shotsApi, ShotsMapper mapper, CacheEndpoint cacheEndpoint) {
        this.shotsApi = shotsApi;
        this.mapper = mapper;
        this.cacheEndpoint = cacheEndpoint;
    }

    public Observable<List<Shot>> getShots() {
        return getShotsData();
    }

    private Observable<List<Shot>> getShotsData() {
        return shotsApi.getShots("", "", "")
                .compose(fromListObservable())
                .map(entity -> mapper.getShot(entity))
                .toList();
    }
}
