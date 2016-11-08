package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import co.netguru.android.inbbbox.model.api.User;
import rx.Observable;

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
    public Observable<User> requestUser() {
        return userApi.getAuthenticatedUser()
                .flatMap(user -> userPrefsRepository.saveUser(user).andThen(Observable.just(user)));
    }
}
