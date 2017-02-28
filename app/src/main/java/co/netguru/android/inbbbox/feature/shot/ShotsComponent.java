package co.netguru.android.inbbbox.feature.shot;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotsAdapter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = ShotsModule.class)
public interface ShotsComponent {
    void inject(ShotsFragment fragment);

    ShotsPresenter getPresenter();

    ShotsAdapter getShotAdapter();
}
