package co.netguru.android.inbbbox.di.component;

import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.controler.notification.NotificationAlarmReceiver;
import co.netguru.android.inbbbox.di.module.ApiModule;
import co.netguru.android.inbbbox.di.module.ApplicationModule;
import co.netguru.android.inbbbox.di.module.ConfigurationModule;

import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.di.module.FollowersFragmentModule;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.di.module.LocalRepositoryModule;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class,
        ConfigurationModule.class,
        ApiModule.class,
        LocalRepositoryModule.class})
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

    LoginComponent plus(LoginModule module);

    MainActivityComponent plusMainActivityComponent();

    SplashScreenComponent plusSplashScreenComponent();

    ShotsComponent plus(ShotsModule shotsModule);

    LikesFragmentComponent plus(LikesFragmentModule module);

    FollowersFragmentComponent plus(FollowersFragmentModule module);

    FollowerDetailsFragmentComponent plus(FollowerDetailsFragmentModule module);

    BucketsFragmentComponent inject();

    void inject(NotificationAlarmReceiver receiver);
}
