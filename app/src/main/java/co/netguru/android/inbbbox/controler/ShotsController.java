package co.netguru.android.inbbbox.controler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.api.FilteredShotsParams;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

@Singleton
public class ShotsController {

    private final ShotsApi shotsApi;
    private final SettingsController settingsController;
    private final LocalTimeFormatter dateFormatter;

    @Inject
    ShotsController(ShotsApi shotsApi,
                    SettingsController settingsController,
                    LocalTimeFormatter dateFormatter) {
        this.shotsApi = shotsApi;
        this.settingsController = settingsController;
        this.dateFormatter = dateFormatter;
    }

    public Observable<List<Shot>> getShots() {
        return settingsController.getStreamSourceSettings()
                .flatMapObservable(this::getShotsObservable);
    }

    private Observable<List<Shot>> getShotsObservable(StreamSourceSettings streamSourceSettings) {
        return selectRequest(streamSourceSettings)
                .compose(fromListObservable())
                .map(Shot::create)
                .distinct()
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

        return Observable.zip(observablesToExecute, this::mergeResults);
    }

    private List<ShotEntity> mergeResults(Object[] args) {
        List<ShotEntity> results = new ArrayList<>();

        for (Object arg : args) {
            results.addAll((List<ShotEntity>) arg);
        }
        return results;
    }

    private Observable<List<ShotEntity>> getFilteredShots(StreamSourceSettings sourceSettings) {
        FilteredShotsParams params = getShotsParams(sourceSettings);
        return shotsApi.getFilteredShots(params.list(),
                params.timeFrame(),
                params.date(),
                params.sort());
    }


    private Observable<List<ShotEntity>> getFollowingShotsData() {
        return shotsApi.getFollowingShots();
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

    public FilteredShotsParams getShotsParams(StreamSourceSettings streamSourceSettings) {
        FilteredShotsParams.Builder builder = FilteredShotsParams.newBuilder();

        boolean wasHandled = false;

        if (streamSourceSettings.isNewToday()) {
            builder.date(dateFormatter.getCurrentDate())
                    .sort(Constants.API.LIST_PARAM_SORT_RECENT_PARAM);
            wasHandled = true;
        }

        if (streamSourceSettings.isPopularToday() && !wasHandled) {
            builder.date(dateFormatter.getCurrentDate())
                    .sort(Constants.API.LIST_PARAM_SORT_VIEWS_PARAM);
            wasHandled = true;
        }

        if (streamSourceSettings.isDebut() && !wasHandled) {
            builder.list(Constants.API.LIST_PARAM_DEBUTS_PARAM);
        }
        return builder.build();
    }
}
