package co.netguru.android.inbbbox.feature.main;

import co.netguru.android.commons.di.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface MainActivityComponent {

    void inject(MainActivity activity);

    MainActivityPresenter getMainActivityPresenter();
}
