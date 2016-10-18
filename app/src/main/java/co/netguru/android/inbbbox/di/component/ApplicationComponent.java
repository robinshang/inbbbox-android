package co.netguru.android.inbbbox.di.component;


import javax.inject.Singleton;

import co.netguru.android.inbbbox.application.DebugMetricsHelper;
import co.netguru.android.inbbbox.di.module.ApplicationModule;
import dagger.Component;

@Singleton
@Component(
        modules = {ApplicationModule.class}
)
public interface ApplicationComponent {

    DebugMetricsHelper getDebugMetricsHelper();

}
