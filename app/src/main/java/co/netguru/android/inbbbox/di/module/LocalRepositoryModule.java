package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.greenrobot.greendao.database.Database;

import javax.inject.Named;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;

import co.netguru.android.inbbbox.model.localrepository.DaoMaster;
import co.netguru.android.inbbbox.model.localrepository.DaoSession;
import dagger.Module;
import dagger.Provides;

@Module
public class LocalRepositoryModule {

    private static final String DATABASE_NAME = "inbbbox-db";
    private static final String GUEST_MODE_SHARED_PREFERENCES_NAME = "guest_mode";
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

    @Named(GUEST_MODE_SHARED_PREFERENCES_NAME)
    @Provides
    @Singleton
    SharedPreferences provideGuestModeSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName()
                .concat(GUEST_MODE_SHARED_PREFERENCES_NAME), Context.MODE_PRIVATE);
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
    UserPrefsRepository provideUserPrefsRepository(
            @Named(USER_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences, Gson gson) {
        return new UserPrefsRepository(sharedPreferences, gson);
    }

    @Provides
    @Singleton
    GuestModeRepository provideGuestModeRepository(
            @Named(GUEST_MODE_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences,
            Gson gson) {
        return new GuestModeRepository(sharedPreferences, gson);
    }

    @Singleton
    @Provides
    Database provideDatabase(App app) {
        return new DaoMaster.DevOpenHelper(app, DATABASE_NAME).getWritableDb();
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(Database database) {
        return new DaoMaster(database).newSession();
    }
}
