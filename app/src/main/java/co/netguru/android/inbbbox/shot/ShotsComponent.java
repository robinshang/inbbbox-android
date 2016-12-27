package co.netguru.android.inbbbox.shot;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.shot.ShotsFragment;
import co.netguru.android.inbbbox.shot.ShotsPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface ShotsComponent {
    void inject(ShotsFragment fragment);

    ShotsPresenter getPresenter();
}
