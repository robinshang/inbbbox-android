package co.netguru.android.inbbbox.app;

import android.content.Context;

import net.hockeyapp.android.CrashManager;

import javax.inject.Inject;
import javax.inject.Singleton;

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
        CrashManager.register(context);
    }

}
