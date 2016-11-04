package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.data.local.UserPrefsController;
import co.netguru.android.inbbbox.models.User;
import rx.Observable;

public class UserProvider {

    private UserApi userApi;
    private UserPrefsController userPrefsController;

    @Inject
    UserProvider(UserApi userApi, UserPrefsController userPrefsController) {
        this.userApi = userApi;
        this.userPrefsController = userPrefsController;
    }

    /**
     * Request user from api
     * Side effect user is saved to prefs
     */
    public Observable<User> requestUser() {
        return userApi.getAuthenticatedUser()
                .doOnNext(userPrefsController::saveUser);
    }
}
