
package co.netguru.android.inbbbox;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import co.netguru.android.inbbbox.di.component.ApplicationComponent;
import co.netguru.android.inbbbox.di.component.DaggerApplicationComponent;
import co.netguru.android.inbbbox.di.component.UserComponent;
import co.netguru.android.inbbbox.di.module.ApplicationModule;
import co.netguru.android.inbbbox.di.module.usermodule.UserModule;
import co.netguru.android.inbbbox.di.module.usermodule.UserModeType;

public class App extends Application {

    private ApplicationComponent appComponent;
    private UserComponent userComponent;

    public static ApplicationComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    public static UserComponent getUserComponent(Context context) {
        return ((App) context.getApplicationContext()).userComponent;
    }

    public static void initUserComponent(Context context, UserModeType userModeType) {
        ((App) context.getApplicationContext()).setupUserComponent(userModeType);
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

    public void setupUserComponent(UserModeType userModeType) {
        this.userComponent = appComponent
                .userComponentBuilder()
                .userModule(new UserModule(userModeType))
                .build();
    }

}