
package co.netguru.android.inbbbox.application;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Base application class
 * Created by lukaszjanyga on 08/09/16.
 */
public class App extends Application {

    private ApplicationComponent appComponent;

    public static ApplicationComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent.getDebugMetricsHelper().init(this);
        Realm.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // init dagger appComponent
        this.appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

}
