package co.netguru.android.inbbbox.app;

import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.app.usercomponent.UserComponent;
import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.feature.remindernotification.NotificationAlarmReceiver;
import co.netguru.android.inbbbox.di.module.ApiModule;
import co.netguru.android.inbbbox.feature.login.LoginModule;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.feature.bucket.BucketsFragmentComponent;
import co.netguru.android.inbbbox.feature.bucket.createbucket.CreateBucketComponent;
import co.netguru.android.inbbbox.feature.bucket.detail.BucketsDetailsComponent;
import co.netguru.android.inbbbox.feature.login.LoginComponent;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewDialogFragmentComponent;
import co.netguru.android.inbbbox.feature.main.MainActivityComponent;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketComponent;
import co.netguru.android.inbbbox.feature.splash.SplashScreenComponent;
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
