package co.netguru.android.inbbbox.shot.detail.fullscreen;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = ShotFullscreenModule.class)
public interface ShotFullscreenComponent {

    void inject(ShotFullscreenFragment fullscreenFragment);

    ShotFullScreenPresenter getPresenter();
}
