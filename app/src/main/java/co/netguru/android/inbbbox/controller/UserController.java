package co.netguru.android.inbbbox.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import co.netguru.android.inbbbox.model.api.UserEntity;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class UserController {

    private UserApi userApi;
    private UserPrefsRepository userPrefsRepository;

    @Inject
    UserController(UserApi userApi, UserPrefsRepository userPrefsRepository) {
        this.userApi = userApi;
        this.userPrefsRepository = userPrefsRepository;
    }

    /**
     * Request user from api
     * Side effect user is saved to prefs
     */
    public Observable<UserEntity> requestUser() {
        return userApi.getAuthenticatedUser()
                .flatMap(user -> userPrefsRepository.saveUser(user).andThen(Observable.just(user)));
    }

    public Single<User> getUserFromCache() {
        return userPrefsRepository
                .getUser()
                .map(User::create);
    }

    public Completable enableGuestMode() {
        return userPrefsRepository.setGuestModeEnabled(true);
    }

    public Single<Boolean> isGuestModeEnabled() {
        return userPrefsRepository.isGuestModeEnabled();
    }
}
