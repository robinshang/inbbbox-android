package co.netguru.android.inbbbox.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Observable;

@Singleton
public class UserShotsController {

    private final ShotsApi shotsApi;

    @Inject
    public UserShotsController(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<List<Shot>> getUserShotsList(long id, int pageNumber, int pageCount) {
        return shotsApi.getUserShots(id, pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .toList();
    }
}
