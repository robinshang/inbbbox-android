package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotsModule {

    private final ShotSwipeListener shotSwipeListener;

    public ShotsModule(ShotSwipeListener shotSwipeListener) {
        this.shotSwipeListener = shotSwipeListener;
    }

    @Provides
    ShotsAdapter provideShotsAdapter() {
        return new ShotsAdapter(shotSwipeListener);
    }
}