package co.netguru.android.inbbbox.splash;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.splash.SplashActivity;
import co.netguru.android.inbbbox.splash.SplashPresenter;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface SplashScreenComponent {
    void inject(SplashActivity activity);

    SplashPresenter getSplashPresenter();
}
