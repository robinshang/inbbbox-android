package co.netguru.android.inbbbox.feature.shot;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
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
    private final ShotPeekAndPop peekAndPop;

    public ShotsModule(ShotSwipeListener shotSwipeListener, DetailsVisibilityChangeEmitter emitter,
                       ShotPeekAndPop peekAndPop) {
        this.shotSwipeListener = shotSwipeListener;
        this.emitter = emitter;
        this.peekAndPop = peekAndPop;
    }

    @Provides
    ShotsAdapter provideShotsAdapter() {
        return new ShotsAdapter(shotSwipeListener, emitter, peekAndPop);
    }
}
