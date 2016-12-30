package co.netguru.android.inbbbox.feature.splash;

import co.netguru.android.commons.di.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface SplashScreenComponent {
    void inject(SplashActivity activity);

    SplashPresenter getSplashPresenter();
}
