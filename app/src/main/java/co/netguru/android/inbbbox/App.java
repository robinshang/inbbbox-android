
package co.netguru.android.inbbbox;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import co.netguru.android.inbbbox.di.component.ApplicationComponent;
import co.netguru.android.inbbbox.di.component.DaggerApplicationComponent;
import co.netguru.android.inbbbox.di.module.ApplicationModule;

public class App extends Application {

    private ApplicationComponent appComponent;

    public static ApplicationComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent.getDebugMetricsHelper().init(this);
        AndroidThreeTen.init(this);
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
