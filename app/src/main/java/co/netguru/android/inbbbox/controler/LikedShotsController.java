package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Observable;
import timber.log.Timber;

@Singleton
public class LikedShotsController {

    private final LikesApi likesApi;
    private final GuestModeController guestModeController;

    @Inject
    LikedShotsController(LikesApi likesApi, GuestModeController guestModeController) {
        this.likesApi = likesApi;
        this.guestModeController = guestModeController;
    }

    public Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount) {
        return Observable.zip(
                guestModeController.getCachedLikedShotIfGuestEnabled(),
                getLikedShotsFromApi(pageNumber, pageCount),
                (cacheShots, apiShots) -> {
                    Timber.d("apiShots %d | cacheShots %d", apiShots.size(), cacheShots.size());
                    cacheShots.addAll(apiShots);
                    return cacheShots;
                });
    }

    private Observable<List<Shot>> getLikedShotsFromApi(int pageNumber, int pageCount) {
        return likesApi.getLikedShots(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(likedShotEntity -> Shot.create(likedShotEntity.shot()))
                .toList();
    }
}
