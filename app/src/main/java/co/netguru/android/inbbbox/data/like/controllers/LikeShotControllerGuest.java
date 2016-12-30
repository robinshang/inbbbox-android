package co.netguru.android.inbbbox.data.like.controllers;

import java.util.List;

import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

public class LikeShotControllerGuest implements LikeShotController {

    private final GuestModeLikesRepository guestModeLikesRepository;
    private final LikesApi likesApi;

    public LikeShotControllerGuest(GuestModeLikesRepository guestModeLikesRepository,
                                   LikesApi likesApi) {
        this.guestModeLikesRepository = guestModeLikesRepository;
        this.likesApi = likesApi;
    }

    @Override
    public Completable isShotLiked(Shot shot) {
        Timber.d("checking is shot liked...");
        return guestModeLikesRepository.isShotLiked(shot)
                .doOnCompleted(() -> Timber.d("Shot is liked"))
                .doOnError(throwable -> Timber.d("Shot is not liked"));
    }

    @Override
    public Completable likeShot(Shot shot) {
        Timber.d("Performing local like action");
        return guestModeLikesRepository.addLikedShot(shot);
    }

    @Override
    public Completable unLikeShot(Shot shot) {
        Timber.d("Performing local unLike action");
        return guestModeLikesRepository.removeLikedShot(shot);
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

    private Observable<List<Shot>> getCachedLikedShot() {
        Timber.d("getting liked shots from cache");
        return guestModeLikesRepository.getLikedShots();
    }
}