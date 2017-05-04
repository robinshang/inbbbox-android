package co.netguru.android.inbbbox.app;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import co.netguru.android.inbbbox.app.usercomponent.UserComponent;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import co.netguru.android.inbbbox.app.usercomponent.UserModule;
import co.netguru.android.inbbbox.common.analytics.AnalyticsModule;

public class App extends Application {

    private ApplicationComponent appComponent;
    private UserComponent userComponent;

    public static ApplicationComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    public static UserComponent getUserComponent(Context context) {
        App app = (App) context.getApplicationContext();

        if (app.userComponent == null) {
            app.appComponent.userComponentRestorer().restoreUserComponent();
        }
        return app.userComponent;
    }

    public static void initUserComponent(Context context, UserModeType userModeType) {
        ((App) context.getApplicationContext()).setupUserComponent(userModeType);
    }

    public static void releaseUserComponent(Context context) {
        ((App) context.getApplicationContext()).userComponent = null;
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
                .analyticsModule(new AnalyticsModule(this))
                .build();
    }

    public void setupUserComponent(UserModeType userModeType) {
        this.userComponent = appComponent
                .userComponentBuilder()
                .userModule(new UserModule(userModeType))
                .build();
    }
}