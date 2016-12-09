package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import rx.Completable;
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

    public Completable.CompletableTransformer getTransformerForShotLike(long id) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(exception -> performLike(exception, id));
    }

    public Completable.CompletableTransformer getTransformerForIsShotLiked(long id) {
        return completable -> completable
                .startWith(isGuestModeDisabled())
                .onErrorResumeNext(e -> checkIsLiked(id));
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
     * logged with i() because exception is expected when guest mode is enabled
     */
    private Completable performLike(Throwable exception, Long id) {
        Timber.i(exception.getMessage());
        return guestModeRepository.addLikeId(id);
    }

    private Completable checkIsLiked(long id) {
        return guestModeRepository.isShotLiked(id)
                .doOnCompleted(() -> Timber.i("Shot is liked"));
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

    private static class GuestModeStateException extends Throwable {

        GuestModeStateException(String message) {
            super(message);
        }
    }
}
