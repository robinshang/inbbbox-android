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

    public Completable.CompletableTransformer getShotLikeTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(exception -> performLike(exception, shot));
    }

    public Completable.CompletableTransformer getIsShotLikedTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(e -> checkIsLiked(shot));
    }

    public Completable.CompletableTransformer getShotUnlikeTransformer(Shot shot) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(exception -> performUnlike(exception, shot));
    }

    public Observable<List<Shot>> getCachedLikedShotIfGuestEnabled() {
        return Observable.just(initList())
                .startWith(isGuestModeDisabled().toObservable())
                .onErrorResumeNext(exception -> getCachedLikedShot());
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
        Timber.d("Performing local like action- " + exception.getMessage());
        return guestModeRepository.addLikedShot(shot);
    }

    private Completable performUnlike(Throwable exception, Shot shot) {
        Timber.d("Performing local unLike action- " + exception.getMessage());
        return guestModeRepository.removeLikedShot(shot);
    }

    private Completable checkIsLiked(Shot shot) {
        Timber.d("checking is shot liked...");
        return guestModeRepository.isShotLiked(shot)
                .doOnCompleted(() -> Timber.d("Shot is liked"))
                .doOnError(throwable -> Timber.d("Shot is not liked"));
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

    private Observable<List<Shot>> getCachedLikedShot() {
        Timber.d("getting liked shots from cache");
        return guestModeRepository.getLikedShots();
    }

    private static class GuestModeStateException extends Throwable {

        GuestModeStateException(String message) {
            super(message);
        }
    }
}
