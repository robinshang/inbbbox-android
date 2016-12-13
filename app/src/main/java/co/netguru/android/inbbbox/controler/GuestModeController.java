package co.netguru.android.inbbbox.controler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

public class GuestModeController {

    private static final String GUEST_MODE_ENABLED_ERROR_MESSAGE = "Guest mode enabled";
    private final UserController userController;
    private final GuestModeRepository guestModeRepository;

    @Inject
    GuestModeController(UserController userController,
                        GuestModeRepository guestModeRepository) {
        this.userController = userController;
        this.guestModeRepository = guestModeRepository;
    }

    public Observable.Transformer<List<Shot>, List<Shot>> getGuestModeCachedShotTransformer() {
        return listObservable -> Observable.zip(
                getCachedLikedShotIfGuestEnabled(),
                listObservable,
                (cacheShots, apiShots) -> {
                    Timber.d("apiShots %d | cacheShots %d", apiShots.size(), cacheShots.size());
                    cacheShots.addAll(apiShots);
                    return cacheShots;
                });
    }

    Completable.CompletableTransformer getShotLikeTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(exception -> performLike(exception, shot));
    }

    Completable.CompletableTransformer getIsShotLikedTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(e -> checkIsLiked(e, shot));
    }

    Completable.CompletableTransformer getShotUnlikeTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(exception -> performUnlike(exception, shot));
    }

    private Observable<List<Shot>> getCachedLikedShotIfGuestEnabled() {
        return Observable.just(initList())
                .startWith(isGuestModeDisabled().toObservable())
                .onErrorResumeNext(this::getCachedLikedShot);
    }

    private List<Shot> initList() {
        return new ArrayList<>();
    }

    /**
     * when guest mode is disabled Completable is complete and stream will follow ONLINE action flow,
     * when guest mode is enabled Completable returns exception and stream will OFFLINE action flow
     * - e.g. like id will be cached in local storage
     */
    private Completable isGuestModeDisabled() {
        return userController.isGuestModeEnabled()
                .flatMapCompletable(this::getGuestModeCompletable);
    }

    /**
     * logged with d() because exception is expected when guest mode is enabled
     */
    private Completable performLike(Throwable exception, Shot shot) {
        Completable resultActionCompletable;
        if (exception instanceof GuestModeStateException) {
            Timber.d("Performing local like action- %s", exception.getMessage());
            resultActionCompletable = guestModeRepository.addLikedShot(shot);
        } else {
            resultActionCompletable = Completable.error(exception);
        }
        return resultActionCompletable;
    }

    private Completable performUnlike(Throwable exception, Shot shot) {
        Completable resultActionCompletable;
        if (exception instanceof GuestModeStateException) {
            Timber.d("Performing local unLike action-  %s", exception.getMessage());
            resultActionCompletable = guestModeRepository.removeLikedShot(shot);
        } else {
            resultActionCompletable = Completable.error(exception);
        }
        return resultActionCompletable;
    }

    private Completable checkIsLiked(Throwable exception, Shot shot) {
        Completable resultActionCompletable;
        if (exception instanceof GuestModeStateException) {
            Timber.d("checking is shot liked...");
            resultActionCompletable = guestModeRepository.isShotLiked(shot)
                    .doOnCompleted(() -> Timber.d("Shot is liked"))
                    .doOnError(throwable -> Timber.d("Shot is not liked"));
        } else {
            resultActionCompletable = Completable.error(exception);
        }
        return resultActionCompletable;
    }

    private Completable getGuestModeCompletable(Boolean guestModeState) {
        Completable completable;
        if (!guestModeState) {
            completable = Completable
                    .complete();
        } else {
            completable = Completable
                    .error(new GuestModeStateException(GUEST_MODE_ENABLED_ERROR_MESSAGE));
        }
        return completable;
    }

    private Observable<List<Shot>> getCachedLikedShot(Throwable exception) {
        Observable<List<Shot>> resultActionObservable;
        if (exception instanceof GuestModeStateException) {
            Timber.d("getting liked shots from cache");
            resultActionObservable = guestModeRepository.getLikedShots();
        } else {
            resultActionObservable = Observable.error(exception);
        }
        return resultActionObservable;

    }

    private static class GuestModeStateException extends Exception {

        GuestModeStateException(String message) {
            super(message);
        }
    }
}
