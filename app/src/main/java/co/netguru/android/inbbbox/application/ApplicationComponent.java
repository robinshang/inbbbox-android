package co.netguru.android.inbbbox.application;


import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.data.api.ApiModule;
import co.netguru.android.inbbbox.db.StorageModule;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.login.LoginComponent;
import co.netguru.android.inbbbox.feature.login.LoginModule;
import dagger.Component;

/**
 * Main application component
 * Created by lukaszjanyga on 08/09/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class,
        ConfigurationModule.class,
        ApiModule.class,
        StorageModule.class})
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

    ErrorMessageParser getApiErrorParser();

    LoginComponent plus(LoginModule module);

}
