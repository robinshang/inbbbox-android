package co.netguru.android.inbbbox.feature.shot;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.shot.recycler.DetailsVisibilityChangeEmitter;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotsAdapter;
import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class ShotsModule {

    private final ShotSwipeListener shotSwipeListener;
    private final DetailsVisibilityChangeEmitter emitter;

    public ShotsModule(ShotSwipeListener shotSwipeListener, DetailsVisibilityChangeEmitter emitter) {
        this.shotSwipeListener = shotSwipeListener;
        this.emitter = emitter;
    }

    @Provides
    ShotsAdapter provideShotsAdapter() {
        return new ShotsAdapter(shotSwipeListener, emitter);
    }
}
