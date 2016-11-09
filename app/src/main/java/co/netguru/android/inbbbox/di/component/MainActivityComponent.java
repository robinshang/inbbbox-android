package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.main.MainActivityPresenter;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface MainActivityComponent {

    void inject(MainActivity activity);

    MainActivityPresenter getMainActivityPresenter();
}
