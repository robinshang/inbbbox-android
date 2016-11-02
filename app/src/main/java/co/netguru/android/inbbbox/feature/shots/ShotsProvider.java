package co.netguru.android.inbbbox.feature.shots;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.FilteredShotsParams;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.models.StreamSourceSettings;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotsProvider {

    private final ShotsApi shotsApi;
    private final ShotsMapper mapper;
    private final DataSource<Settings> settingsDataSource;
    private final ShotsParamsFactory shotsParamFactory;

    @Inject
    ShotsProvider(ShotsApi shotsApi,
                  ShotsMapper mapper,
                  DataSource<Settings> cacheEndpoint,
                  ShotsParamsFactory shotsRequestFactory) {
        this.shotsApi = shotsApi;
        this.mapper = mapper;
        this.settingsDataSource = cacheEndpoint;
        this.shotsParamFactory = shotsRequestFactory;
    }

    public Observable<List<Shot>> getShots() {
        return getSettings().flatMap(this::getShotsObservable);
    }

    private Observable<List<Shot>> getShotsObservable(Settings settings) {
        return selectRequest(settings.getStreamSourceSettings())
                .compose(fromListObservable())
                .map(mapper::getShot)
                .toList();
    }

    private Observable<List<ShotEntity>> selectRequest(StreamSourceSettings sourceSettings) {
        List<Observable<List<ShotEntity>>> observablesToExecute = new ArrayList<>();
        if (sourceSettings.isFollowing()) {
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

    private Observable<List<ShotEntity>> getFilteredShots(StreamSourceSettings sourceSettings) {
        FilteredShotsParams params = shotsParamFactory.getShotsParams(sourceSettings);
        return shotsApi.getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }


    private Observable<List<ShotEntity>> getFollowingShotsData() {
        return shotsApi.getFollowingShots();
    }

    private Observable<Settings> getSettings() {
        return settingsDataSource.get();
    }

    private int getRequestCount(StreamSourceSettings sourceSettings) {
        int count = 0;
        if (sourceSettings.isPopularToday()) {
            count++;
        }

        if (sourceSettings.isDebut()) {
            count++;
        }

        if (sourceSettings.isNewToday()) {
            count++;
        }
        return count;
    }
}
