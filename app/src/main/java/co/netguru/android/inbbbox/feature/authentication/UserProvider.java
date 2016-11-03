package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

public class UserProvider {

    private UserApi userApi;
    private DataSource<User> dataSource;

    @Inject
    UserProvider(UserApi userApi, DataSource<User> dataSource) {
        this.userApi = userApi;
        this.dataSource = dataSource;
    }

    public Observable<Boolean> getUser() {
        return userApi.getAuthenticatedUser()
                .concatMap(dataSource::save);
    }
}
