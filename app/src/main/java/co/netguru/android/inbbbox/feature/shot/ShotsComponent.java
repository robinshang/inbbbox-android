package co.netguru.android.inbbbox.feature.shot;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface ShotsComponent {
    void inject(ShotsFragment fragment);

    ShotsPresenter getPresenter();
}
