package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.functions.Action1;

public class UserProvider {

    private static User user = null;
    private UserApi userApi;
    private CacheEndpoint cacheEndpoint;

    @Inject
    public UserProvider(UserApi userApi, CacheEndpoint cacheEndpoint) {
        this.userApi = userApi;
        this.cacheEndpoint = cacheEndpoint;
    }

    public Observable<User> getUser() {
        return userApi.getAuthenticatedUser()
                .doOnNext(user1 -> cacheEndpoint.save(Constants.Db.CURRENT_USER, user1));
    }
}
