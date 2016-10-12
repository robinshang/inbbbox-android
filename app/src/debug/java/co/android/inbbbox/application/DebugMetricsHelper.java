

package co.android.inbbbox.application;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.nshmura.strictmodenotifier.StrictModeNotifier;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import co.netguru.android.inbbbox.application.App;

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
 * Created by lukaszjanyga on 08/09/16.
 */
public class DebugMetricsHelper {

    @Inject
    public DebugMetricsHelper() {
    }

    public void init(Context context) {
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
    }

}
