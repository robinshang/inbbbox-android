package co.netguru.android.inbbbox.controler.likescontroller;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

public class LikeShotControllerGuest implements LikeShotController {

    private static final String GUEST_MODE_ENABLED_ERROR_MESSAGE = "Guest mode enabled";
    private final GuestModeRepository guestModeRepository;
    private final LikesApi likesApi;

    @Inject
    public LikeShotControllerGuest(GuestModeRepository guestModeRepository,
                                   LikesApi likesApi) {
        this.guestModeRepository = guestModeRepository;
        this.likesApi = likesApi;
    }

    @Override
    public Completable isShotLiked(Shot shot) {
        return checkIsLiked(shot);
    }

    @Override
    public Completable likeShot(Shot shot) {
        return performLike(shot);
    }

    @Override
    public Completable unLikeShot(Shot shot) {
        return performUnlike(shot);
    }

    @Override
    public Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount) {
        return Observable.zip(
                getCachedLikedShot(),
                getLikesFromApi(pageNumber, pageCount),
                (cacheShots, apiShots) -> {
                    Timber.d("apiShots %d | cacheShots %d", apiShots.size(), cacheShots.size());
                    cacheShots.addAll(apiShots);
                    return cacheShots;
                });
    }

    private Observable<List<Shot>> getLikesFromApi(int pageNumber, int pageCount) {
        return likesApi.getLikedShots(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(likedShotEntity -> Shot.create(likedShotEntity.shot()))
                .toList();
    }

    private Completable performLike(Shot shot) {
        Timber.d("Performing local like action");
        return guestModeRepository.addLikedShot(shot);
    }

    private Completable performUnlike(Shot shot) {
        Timber.d("Performing local unLike action");
        return guestModeRepository.removeLikedShot(shot);
    }

    private Completable checkIsLiked(Shot shot) {
        Timber.d("checking is shot liked...");
        return guestModeRepository.isShotLiked(shot)
                .doOnCompleted(() -> Timber.d("Shot is liked"))
                .doOnError(throwable -> Timber.d("Shot is not liked"));
    }

    private Observable<List<Shot>> getCachedLikedShot() {
        Timber.d("getting liked shots from cache");
        return guestModeRepository.getLikedShots();

    }
}
