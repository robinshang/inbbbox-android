package co.netguru.android.inbbbox.shot.detail;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@Subcomponent(modules = ShotsDetailsModule.class)
@FragmentScope
public interface ShotDetailsComponent {
    void inject(ShotDetailsFragment fragment);

    ShotDetailsPresenter getPresenter();
}
