package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.data.models.User;
import rx.Observable;

public class UserProvider {

    private static User user = null;
    private UserApi userApi;

    @Inject
    public UserProvider(UserApi userApi) {
        this.userApi = userApi;
    }

    public Observable<User> getUser() {
        Observable<User> dataObservable = null;
        // TODO: 12.10.2016 handling user data flow
        return dataObservable;

    }
}
