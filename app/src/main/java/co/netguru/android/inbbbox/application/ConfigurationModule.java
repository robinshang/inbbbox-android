package co.netguru.android.inbbbox.application;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.application.configuration.RequestInterceptor;
import co.netguru.android.inbbbox.db.RealmStorage;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ConfigurationModule {

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Singleton
    @Provides
    RealmConfiguration provideRealmStorage() {
        return new RealmConfiguration.Builder().build();
    }

}
