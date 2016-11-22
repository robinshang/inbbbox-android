package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.details.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.details.ShotDetailsPresenter;
import dagger.Subcomponent;

@Subcomponent(modules = ShotsDetailsModule.class)
@FragmentScope
public interface ShotDetailsComponent {
    void inject(ShotDetailsFragment fragment);

    ShotDetailsPresenter getPresenter();
}
