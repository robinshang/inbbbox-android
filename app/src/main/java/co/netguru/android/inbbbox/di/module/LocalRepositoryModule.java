package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class LocalRepositoryModule {

    private static final String SETTINGS_SHARED_PREFERENCES_NAME = "settings";
    private static final String TOKEN_SHARED_PREFERENCES_NAME = "token";
    private static final String USER_SHARED_PREFERENCES_NAME = "user";

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
    SettingsPrefsRepository provideSettingsPrefsRepository(
            @Named(SETTINGS_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new SettingsPrefsRepository(sharedPreferences);
    }

    @Provides
    TokenPrefsRepository provideTokenPrefsRepository(
            @Named(TOKEN_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new TokenPrefsRepository(sharedPreferences);
    }

    @Provides
    UserPrefsRepository provideUserPrefsRepository(
            @Named(USER_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences, Gson gson) {
        return new UserPrefsRepository(sharedPreferences, gson);
    }
}
