package co.netguru.android.inbbbox.di.component;

import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.di.module.ApiModule;
import co.netguru.android.inbbbox.di.module.ApplicationModule;
import co.netguru.android.inbbbox.di.module.ConfigurationModule;
import co.netguru.android.inbbbox.di.module.DataSourceModule;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.di.module.MainActivityModule;
import co.netguru.android.inbbbox.di.module.StorageModule;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;

import co.netguru.android.inbbbox.feature.notification.NotificationAlarmReceiver;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class,
        ConfigurationModule.class,
        ApiModule.class,
        StorageModule.class,
        DataSourceModule.class})
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

    ErrorMessageParser getApiErrorParser();

    LoginComponent plus(LoginModule module);

    MainActivityComponent plus(MainActivityModule module);

    ShotsComponent plusShotsComponent();

    void inject(NotificationAlarmReceiver receiver);
}
