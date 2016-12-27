package co.netguru.android.inbbbox.data.shot;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

@Singleton
public class ShotsController {

    private final ShotsApi shotsApi;
    private final SettingsController settingsController;

    @Inject
    ShotsController(ShotsApi shotsApi,
                    SettingsController settingsController) {
        this.shotsApi = shotsApi;
        this.settingsController = settingsController;
    }

    public Observable<List<Shot>> getShots(int pageNumber, int pageCount) {
        return settingsController.getStreamSourceSettings()
                .flatMapObservable(settings -> getShotsObservable(settings, pageNumber, pageCount));
    }

    private Observable<List<Shot>> getShotsObservable(StreamSourceSettings streamSourceSettings, int pageNumber, int pageCount) {
        return selectRequest(streamSourceSettings, pageNumber, pageCount)
                .compose(fromListObservable())
                .map(Shot::create)
                .distinct()
                .toList();
    }

    private Observable<List<ShotEntity>> selectRequest(StreamSourceSettings sourceSettings, int pageNumber, int pageCount) {
        final List<Observable<List<ShotEntity>>> observablesToExecute = new LinkedList<>();
        if (sourceSettings.isFollowing()) {
            observablesToExecute.add(shotsApi.getFollowingShots(pageNumber, pageCount));
        }
        if (sourceSettings.isNewToday()) {
            addShotsSortedByDate(observablesToExecute, pageNumber, pageCount);
        }
        if (sourceSettings.isPopularToday()) {
            addShotsSortedByDate(observablesToExecute, pageNumber, pageCount);
        }
        if (sourceSettings.isDebut()) {
            observablesToExecute.add(shotsApi.getShotsByList(Constants.API.LIST_PARAM_DEBUTS_PARAM, pageNumber, pageCount));
        }

        return Observable.zip(observablesToExecute, this::mergeResults);
    }

    private void addShotsSortedByDate(List<Observable<List<ShotEntity>>> observablesToExecute, int pageNumber, int pageCount) {
        observablesToExecute.add(shotsApi.getShotsByDateSort(DateTimeFormatUtil.getCurrentDate(),
                Constants.API.LIST_PARAM_SORT_RECENT_PARAM, pageNumber, pageCount));
    }

    private List<ShotEntity> mergeResults(Object[] args) {
        List<ShotEntity> results = new LinkedList<>();

        for (Object arg : args) {
            results.addAll((List<ShotEntity>) arg);
        }
        return results;
    }
}
