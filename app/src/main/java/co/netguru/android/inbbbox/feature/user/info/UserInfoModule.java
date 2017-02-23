package co.netguru.android.inbbbox.feature.user.info;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import dagger.Module;
import dagger.Provides;

@Module
public class UserInfoModule {

    private UserWithShots user;

    public UserInfoModule(UserWithShots user) {
        this.user = user;
    }

    @Provides
    @FragmentScope
    public UserWithShots provideUser() {
        return user;
    }
}
