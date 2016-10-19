package co.netguru.android.inbbbox.application;

import android.content.Context;

import javax.inject.Inject;
/**
 * Helper class that initializes a set of debugging tools
 * for the debug build type and does nothing for the release type.
 * <p>
 * Tools:
 * <ul>
 * <li> AndroidDevMetrics
 * <li> Stetho
 * <li> StrictMode
 * <li> LeakCanary
 * </ul>
 * <p>
 */
public class DebugMetricsHelper {

    @Inject
    public DebugMetricsHelper() {
    }

    public void init(Context context) {
        //dummy - no debug tools for release builds
    }

}
