package co.netguru.android.inbbbox.feature.user.info.singleuser;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import dagger.Module;
import dagger.Provides;

@Module
public class UserInfoModule {

    private final User user;

    public UserInfoModule(User user) {
        this.user = user;
    }

    @Provides
    User provideUser() {
        return user;
    }
}