package co.netguru.android.inbbbox.main;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.main.MainActivity;
import co.netguru.android.inbbbox.main.MainActivityPresenter;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface MainActivityComponent {

    void inject(MainActivity activity);

    MainActivityPresenter getMainActivityPresenter();
}
