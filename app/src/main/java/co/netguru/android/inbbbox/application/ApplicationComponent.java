
package co.netguru.android.inbbbox.application;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Main application component
 * Created by lukaszjanyga on 08/09/16.
 */
@Singleton
@Component
public interface ApplicationComponent {

    DebugMetricsHelper getDebugMetricsHelper();

}
