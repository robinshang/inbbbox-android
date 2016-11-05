package co.netguru.android.inbbbox.di.module;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApplicationModule {

    public static final String SETTINGS_SHARED_PREFERENCES_NAME = "settings";
    public static final String TOKEN_SHARED_PREFERENCES_NAME = "token";
    public static final String USER_SHARED_PREFERENCES_NAME = "user";


    private Application application;

    public ApplicationModule(Application application) {

        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    Resources provideResources() {
        return application.getResources();
    }

    @Named(SETTINGS_SHARED_PREFERENCES_NAME)
    @Provides
    SharedPreferences provideSettingsSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName().concat(SETTINGS_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Named(TOKEN_SHARED_PREFERENCES_NAME)
    @Provides
    SharedPreferences provideTokenSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName().concat(TOKEN_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Named(USER_SHARED_PREFERENCES_NAME)
    @Provides
    SharedPreferences provideUserSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName().concat(USER_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Provides
    NotificationManager provideNotificationManager() {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    AlarmManager provideAlarmManager() {
        return (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
    }
}
