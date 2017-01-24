package co.netguru.android.inbbbox.app;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.nshmura.strictmodenotifier.StrictModeNotifier;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Helper class that initializes a set of debugging tools
 * for the debug build type and register crash manager for release type.
 * <p>
 * Debug type tools:
 * <ul>
 * <li> AndroidDevMetrics
 * <li> Stetho
 * <li> StrictMode
 * <li> LeakCanary
 * <li> Timber
 * </ul>
 * Release type tools:
 * <ul>
 * <li> CrashManager
 * </ul>
 * <p>
 */
@Singleton
class DebugMetricsHelper {

    @Inject
    DebugMetricsHelper() {
        //DI
    }

    void init(Context context) {
        // AndroidDevMetrics
        AndroidDevMetrics.initWith(context);

        // Stetho
        Stetho.initialize(Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build());

        // StrictMode
        StrictModeNotifier.install(context);
        new Handler().post(() -> {
            final StrictMode.ThreadPolicy threadPolicy =
                    new StrictMode.ThreadPolicy.Builder().detectAll()
                            .permitDiskReads()
                            .permitDiskWrites()
                            .penaltyLog() // Must!
                            .build();
            StrictMode.setThreadPolicy(threadPolicy);

            final StrictMode.VmPolicy vmPolicy =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog() // Must!
                            .build();
            StrictMode.setVmPolicy(vmPolicy);
        });

        // LeakCanary
        LeakCanary.install((App) context.getApplicationContext());

        //Timber
        Timber.plant(new Timber.DebugTree());

        //BlockCanary
//        BlockCanary.install(context, new BlockCanaryContext()).start();
    }

}
