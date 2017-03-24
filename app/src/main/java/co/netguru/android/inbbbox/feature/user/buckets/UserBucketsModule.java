package co.netguru.android.inbbbox.feature.user.buckets;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import dagger.Module;
import dagger.Provides;

@Module
@FragmentScope
public class UserBucketsModule {

    private final User user;

    public UserBucketsModule(User user) {
        this.user = user;
    }

    @Provides
    User provideUser() {
        return user;
    }
}
