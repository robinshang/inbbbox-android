package co.netguru.android.inbbbox.controler;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.DateTimeFormatUtil;
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
        final List<Observable<List<ShotEntity>>> observablesToExecute = new LinkedList<>();
        if (sourceSettings.isFollowing()) {
            observablesToExecute.add(shotsApi.getFollowingShots());
        }
        if (sourceSettings.isNewToday()) {
            observablesToExecute.add(shotsApi.getShotsByDateSort(DateTimeFormatUtil.getCurrentDate(),
                    Constants.API.LIST_PARAM_SORT_RECENT_PARAM));
        }
        if (sourceSettings.isPopularToday()) {
            observablesToExecute.add(shotsApi.getShotsByDateSort(DateTimeFormatUtil.getCurrentDate(),
                    Constants.API.LIST_PARAM_SORT_VIEWS_PARAM));
        }
        if (sourceSettings.isDebut()) {
            observablesToExecute.add(shotsApi.getShotsByList(Constants.API.LIST_PARAM_DEBUTS_PARAM));
        }

        return Observable.zip(observablesToExecute, this::mergeResults);
    }

    private List<ShotEntity> mergeResults(Object[] args) {
        List<ShotEntity> results = new LinkedList<>();

        for (Object arg : args) {
            results.addAll((List<ShotEntity>) arg);
        }
        return results;
    }
}
