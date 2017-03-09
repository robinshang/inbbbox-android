package co.netguru.android.inbbbox.feature.user.info.team;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import dagger.Module;
import dagger.Provides;

@Module
public class TeamInfoModule {

    private User user;

    public TeamInfoModule(User user) {
        this.user = user;
    }

    @Provides
    @FragmentScope
    public User provideUser() {
        return user;
    }
}
