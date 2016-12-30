package co.netguru.android.inbbbox.data.dribbbleuser.user;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class UserController {

    private UserApi userApi;
    private CurrentUserPrefsRepository currentUserPrefsRepository;

    @Inject
    UserController(UserApi userApi, CurrentUserPrefsRepository currentUserPrefsRepository) {
        this.userApi = userApi;
        this.currentUserPrefsRepository = currentUserPrefsRepository;
    }

    /**
     * Request user from api
     * Side effect user is saved to prefs
     */
    public Observable<UserEntity> requestUser() {
        return userApi.getAuthenticatedUser()
                .flatMap(user -> currentUserPrefsRepository.saveUser(user).andThen(Observable.just(user)));
    }

    public Single<User> getUserFromCache() {
        return currentUserPrefsRepository
                .getUser()
                .map(user -> User.create(user, null));
    }

    public Completable enableGuestMode() {
        return currentUserPrefsRepository.setGuestModeEnabled(true);
    }

    public Single<Boolean> isGuestModeEnabled() {
        return currentUserPrefsRepository.isGuestModeEnabled();
    }
}
