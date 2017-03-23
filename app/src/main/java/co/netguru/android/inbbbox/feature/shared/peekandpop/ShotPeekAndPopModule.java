package co.netguru.android.inbbbox.feature.shared.peekandpop;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotPeekAndPopModule {

    private final Shot shot;

    public ShotPeekAndPopModule(Shot shot) {
        this.shot = shot;
    }

    @Provides
    @FragmentScope
    public Shot provideShot() {
        return shot;
    }
}
