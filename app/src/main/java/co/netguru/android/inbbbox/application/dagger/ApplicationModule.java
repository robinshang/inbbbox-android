/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.application.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    ApplicationModule(Application application) {

        this.application = application;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    Resources provideResources() {
        return application.getResources();
    }
}
