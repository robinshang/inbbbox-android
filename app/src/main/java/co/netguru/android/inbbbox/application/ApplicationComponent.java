package co.netguru.android.inbbbox.application;


import javax.inject.Singleton;

import co.netguru.android.commons.di.BaseComponent;
import dagger.Component;

/**
 * Main application component
 * Created by lukaszjanyga on 08/09/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ConfigurationModule.class})
public interface ApplicationComponent extends BaseComponent {

    DebugMetricsHelper getDebugMetricsHelper();

}
