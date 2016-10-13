package co.netguru.android.inbbbox.application;


import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.data.api.AuthorizeApiModule;
import co.netguru.android.inbbbox.db.CacheEndPointModule;
import co.netguru.android.inbbbox.feature.login.LoginComponent;
import co.netguru.android.inbbbox.feature.login.LoginModule;
import co.netguru.android.inbbbox.utils.ApiErrorParser;
import dagger.Component;

/**
 * Main application component
 * Created by lukaszjanyga on 08/09/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class,
        ConfigurationModule.class,
        AuthorizeApiModule.class,
        CacheEndPointModule.class})
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

    ApiErrorParser getApiErrorParser();

    LoginComponent plus(LoginModule module);

}
