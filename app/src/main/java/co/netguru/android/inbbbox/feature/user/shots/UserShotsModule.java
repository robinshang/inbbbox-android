package co.netguru.android.inbbbox.feature.user.shots;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import dagger.Module;
import dagger.Provides;

@Module
public class UserShotsModule {

    private UserWithShots userWithShots;

    public UserShotsModule(UserWithShots userWithShots) {
        this.userWithShots = userWithShots;
    }

    @Provides
    @FragmentScope
    public UserWithShots provideUserWithShots() {
        return userWithShots;
    }
}
