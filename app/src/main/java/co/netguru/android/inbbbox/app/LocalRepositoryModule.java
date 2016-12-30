package co.netguru.android.inbbbox.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.greenrobot.greendao.database.Database;

import javax.inject.Named;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.db.DaoMaster;
import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.dribbbleuser.user.CurrentUserPrefsRepository;
import co.netguru.android.inbbbox.data.session.TokenPrefsRepository;
import co.netguru.android.inbbbox.data.settings.SettingsPrefsRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class LocalRepositoryModule {

    private static final String DATABASE_NAME = "inbbbox-db";
    private static final String SETTINGS_SHARED_PREFERENCES_NAME = "settings";
    private static final String TOKEN_SHARED_PREFERENCES_NAME = "token";
    private static final String USER_SHARED_PREFERENCES_NAME = "user";

    @Named(SETTINGS_SHARED_PREFERENCES_NAME)
    @Provides
    @Singleton
    SharedPreferences provideSettingsSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName()
                .concat(SETTINGS_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Named(TOKEN_SHARED_PREFERENCES_NAME)
    @Provides
    @Singleton
    SharedPreferences provideTokenSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName()
                .concat(TOKEN_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Named(USER_SHARED_PREFERENCES_NAME)
    @Provides
    @Singleton
    SharedPreferences provideUserSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName()
                .concat(USER_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SettingsPrefsRepository provideSettingsPrefsRepository(
            @Named(SETTINGS_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new SettingsPrefsRepository(sharedPreferences);
    }

    @Provides
    @Singleton
    TokenPrefsRepository provideTokenPrefsRepository(
            @Named(TOKEN_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new TokenPrefsRepository(sharedPreferences);
    }

    @Provides
    @Singleton
    CurrentUserPrefsRepository provideUserPrefsRepository(
            @Named(USER_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences, Gson gson) {
        return new CurrentUserPrefsRepository(sharedPreferences, gson);
    }

    @Singleton
    @Provides
    Database provideDatabase(Application application) {
        return new DaoMaster.DevOpenHelper(application, DATABASE_NAME).getWritableDb();
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(Database database) {
        return new DaoMaster(database).newSession();
    }
}
