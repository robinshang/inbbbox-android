package co.netguru.android.inbbbox.di.module;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.local.SettingsPrefsController;
import co.netguru.android.inbbbox.data.local.TokenPrefsController;
import co.netguru.android.inbbbox.data.local.UserPrefsController;
import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class DataSourceModule {

    @Provides
    SettingsPrefsController provideSettingsPrefsController(
            @Named(ApplicationModule.SETTINGS_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new SettingsPrefsController(sharedPreferences);
    }

    @Provides
    TokenPrefsController provideTokenPrefsController(
            @Named(ApplicationModule.TOKEN_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences) {
        return new TokenPrefsController(sharedPreferences);
    }

    @Provides
    UserPrefsController provideUserPrefsController(
            @Named(ApplicationModule.USER_SHARED_PREFERENCES_NAME) SharedPreferences sharedPreferences, Gson gson) {
        return new UserPrefsController(sharedPreferences, gson);
    }

}
