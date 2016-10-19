package co.netguru.android.inbbbox.di.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {

        this.application = application;
    }


    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    Resources provideResources() {
        return application.getResources();
    }
}
