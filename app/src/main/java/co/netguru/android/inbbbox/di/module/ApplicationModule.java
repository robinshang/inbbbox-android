package co.netguru.android.inbbbox.di.module;

import android.content.Context;

import dagger.Module;

@Module
public class ApplicationModule {

    private Context applicationContext;

    public ApplicationModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }
}
