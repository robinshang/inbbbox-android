/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.application;


import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.nshmura.strictmodenotifier.StrictModeNotifier;
import com.squareup.leakcanary.LeakCanary;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.application.dagger.ApplicationComponent;

public class InbbboxApp extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initDebugToolsIfNeed();
    }

    private void initDebugToolsIfNeed() {
        if (BuildConfig.DEBUG) {
            initDevMetrics();
            initSteho();
            initStrictMode();
            initLeakCanary();
        }
    }

    private void initLeakCanary() {
        LeakCanary.install(this);

    }

    private void initStrictMode() {
        StrictModeNotifier.install(this);
        StrictModeNotifier.install(this);
        new Handler().post(() -> {
            final StrictMode.ThreadPolicy threadPolicy =
                    new StrictMode.ThreadPolicy.Builder().detectAll()
                            .permitDiskReads()
                            .permitDiskWrites()
                            .penaltyLog() // Must!
                            .build();
            StrictMode.setThreadPolicy(threadPolicy);

            final StrictMode.VmPolicy vmPolicy =
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog() // Must!
                            .build();
            StrictMode.setVmPolicy(vmPolicy);
        });
    }

    private void initSteho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }

    private void initDevMetrics() {
        AndroidDevMetrics.initWith(this);
    }


    public static ApplicationComponent getAppComponent(Context context) {
        return ((InbbboxApp) context.getApplicationContext()).getAppComponent();
    }

    private ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
