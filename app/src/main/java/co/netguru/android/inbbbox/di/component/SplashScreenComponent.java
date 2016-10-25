package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.splash.SplashActivity;
import co.netguru.android.inbbbox.feature.splash.SplashPresenter;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface SplashScreenComponent {
    void inject(SplashActivity activity);

    SplashPresenter getSplashPresenter();
}
