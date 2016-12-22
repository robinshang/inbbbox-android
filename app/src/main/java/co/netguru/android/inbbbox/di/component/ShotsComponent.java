package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shots.ShotsFragment;
import co.netguru.android.inbbbox.feature.shots.ShotsPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface ShotsComponent {
    void inject(ShotsFragment fragment);

    ShotsPresenter getPresenter();
}
