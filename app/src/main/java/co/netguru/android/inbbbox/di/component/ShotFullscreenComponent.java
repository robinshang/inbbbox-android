package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.di.module.ShotFullscreenModule;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullScreenPresenter;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullscreenFragment;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = ShotFullscreenModule.class)
public interface ShotFullscreenComponent {

    void inject(ShotFullscreenFragment fullscreenFragment);

    ShotFullScreenPresenter getPresenter();
}
