package co.netguru.android.inbbbox.di.component;

import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.controler.notification.NotificationAlarmReceiver;
import co.netguru.android.inbbbox.di.module.ApiModule;
import co.netguru.android.inbbbox.di.module.ApplicationModule;
import co.netguru.android.inbbbox.di.module.ConfigurationModule;
import co.netguru.android.inbbbox.di.module.LocalRepositoryModule;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.event.RxBus;
import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ConfigurationModule.class,
                ApiModule.class,
                LocalRepositoryModule.class
        })
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

    UserComponent.Builder userComponentBuilder();

    LoginComponent plus(LoginModule module);

    OauthWebViewDialogFragmentComponent plusOauthWebViewDialogFragmentComponent();

    MainActivityComponent plusMainActivityComponent();

    SplashScreenComponent plusSplashScreenComponent();

    BucketsFragmentComponent plusBucketsFragmentComponent();

    BucketsDetailsComponent plusBucketDetailsComponent();

    AddToBucketComponent plusAddToBucketComponent();

    CreateBucketComponent plusCreateBucketComponent();

    RxBus rxBus();

    void inject(NotificationAlarmReceiver receiver);
}
