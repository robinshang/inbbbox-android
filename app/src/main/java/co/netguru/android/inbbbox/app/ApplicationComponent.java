package co.netguru.android.inbbbox.app;

import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.notification.NotificationAlarmReceiver;
import co.netguru.android.inbbbox.di.module.ApiModule;
import co.netguru.android.inbbbox.login.LoginModule;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.bucket.BucketsFragmentComponent;
import co.netguru.android.inbbbox.bucket.createbucket.CreateBucketComponent;
import co.netguru.android.inbbbox.bucket.detail.BucketsDetailsComponent;
import co.netguru.android.inbbbox.login.LoginComponent;
import co.netguru.android.inbbbox.login.oauthwebview.OauthWebViewDialogFragmentComponent;
import co.netguru.android.inbbbox.main.MainActivityComponent;
import co.netguru.android.inbbbox.shot.addtobucket.AddToBucketComponent;
import co.netguru.android.inbbbox.splash.SplashScreenComponent;
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
