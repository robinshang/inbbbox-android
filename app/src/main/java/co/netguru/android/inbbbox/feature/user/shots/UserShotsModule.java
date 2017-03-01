package co.netguru.android.inbbbox.feature.user.shots;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import dagger.Module;
import dagger.Provides;

@Module
public class UserShotsModule {

    private User user;

    public UserShotsModule(User user) {
        this.user = user;
    }

    @Provides
    @FragmentScope
    public User provideUserWithShots() {
        return user;
    }
}
