package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.models.StreamSourceState;
import co.netguru.android.inbbbox.data.viewmodels.Shot;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.functions.Func1;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotsProvider {

    private ShotsApi shotsApi;
    private ShotsMapper mapper;
    private CacheEndpoint<Settings> cacheEndpoint;
    private ShotsRequestFactory shotsRequestFactory;

    @Inject
    ShotsProvider(ShotsApi shotsApi, ShotsMapper mapper, CacheEndpoint cacheEndpoint,
                  ShotsRequestFactory shotsRequestFactory) {
        this.shotsApi = shotsApi;
        this.mapper = mapper;
        this.cacheEndpoint = cacheEndpoint;
        this.shotsRequestFactory = shotsRequestFactory;
    }

    public Observable<List<Shot>> getShots() {
        return getSettings().flatMap(new Func1<Settings, Observable<List<Shot>>>() {
            @Override
            public Observable<List<Shot>> call(Settings settings) {
                return getShotsObservable(settings);
            }
        });
    }

    private Observable<List<Shot>> getShotsObservable(Settings settings) {
        return selectRequest(settings.getStreamSourceState())
                .compose(fromListObservable())
                .map(entity -> mapper.getShot(entity))
                .toList();
    }

    private Observable<List<ShotEntity>> selectRequest(StreamSourceState sourceSettings) {
        Observable observable = null;
        if (sourceSettings.getDebut()) {
            observable = getFollowingShotsData();
        } else {
            observable = getFilteredShots();
        }
        return observable;
    }

    private Observable getFilteredShots() {
        return null;
    }


    private Observable<List<ShotEntity>> getFollowingShotsData() {
        // TODO: 19.10.2016 setparameters from params factory
        return shotsApi.getFilteredShots();
    }

    private Observable<Settings> getSettings() {
        // TODO: 19.10.2016 setparameters from params factory
        return cacheEndpoint.get(Constants.Db.SETTINGS_KEY, Settings.class);
    }
}
