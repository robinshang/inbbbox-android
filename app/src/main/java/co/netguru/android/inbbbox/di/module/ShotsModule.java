package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotsModule {

    @Provides
    ShotsAdapter provideShotsAdapter() {
        return new ShotsAdapter();
    }
}