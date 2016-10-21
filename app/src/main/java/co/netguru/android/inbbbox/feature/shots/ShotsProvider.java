package co.netguru.android.inbbbox.feature.shots;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.models.StreamSourceState;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotsProvider {

    private ShotsApi shotsApi;
    private ShotsMapper mapper;
    private DataSource<Settings> settingsDataSource;
    private ShotsRequestFactory shotsRequestFactory;

    @Inject
    ShotsProvider(ShotsApi shotsApi,
                  ShotsMapper mapper,
                  DataSource<Settings> cacheEndpoint,
                  ShotsRequestFactory shotsRequestFactory) {
        this.shotsApi = shotsApi;
        this.mapper = mapper;
        this.settingsDataSource = cacheEndpoint;
        this.shotsRequestFactory = shotsRequestFactory;
    }

    public Observable<List<Shot>> getShots() {
        return getSettings().flatMap(this::getShotsObservable);
    }

    private Observable<List<Shot>> getShotsObservable(Settings settings) {
        return selectRequest(settings.getStreamSourceState())
                .compose(fromListObservable())
                .map(entity -> mapper.getShot(entity))
                .toList();
    }

    private Observable<List<ShotEntity>> selectRequest(StreamSourceState sourceSettings) {
        List<Observable<List<ShotEntity>>> observablesToExecute = new ArrayList<>();
        if (sourceSettings.getFollowingState()) {
            observablesToExecute.add(getFollowingShotsData());
        }

        for (int i = 0; i < getRequestCount(sourceSettings); i++) {
            observablesToExecute.add(getFilteredShots(sourceSettings));
        }

        return Observable.zip(observablesToExecute, this::margeResults);
    }

    private List<ShotEntity> margeResults(Object[] args) {
        List<ShotEntity> results = new ArrayList<>();

        for (Object arg : args) {
            results.addAll((List<ShotEntity>) arg);
        }
        return results;
    }

    private Observable<List<ShotEntity>> getFilteredShots(StreamSourceState sourceSettings) {
        return shotsApi.getFilteredShots(shotsRequestFactory.getShotsParams(sourceSettings));
    }


    private Observable<List<ShotEntity>> getFollowingShotsData() {
        return shotsApi.getFollowingShots();
    }

    private Observable<Settings> getSettings() {
        return settingsDataSource.get();
    }

    private int getRequestCount(StreamSourceState sourceSettings) {
        int count = 0;
        if (sourceSettings.getPopularTodayState()) {
            count++;
        }

        if (sourceSettings.getDebut()) {
            count++;
        }

        if (sourceSettings.getNewTodayState()) {
            count++;
        }
        return count;
    }
}
