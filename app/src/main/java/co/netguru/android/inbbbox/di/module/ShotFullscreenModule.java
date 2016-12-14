package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullscreenAdapter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotFullscreenModule {

    public ShotFullscreenModule() {

    }

    @Provides
    ShotFullscreenAdapter provideShotsAdapter() {
        return new ShotFullscreenAdapter();
    }
}