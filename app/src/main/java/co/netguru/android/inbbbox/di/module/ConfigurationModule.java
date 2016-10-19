package co.netguru.android.inbbbox.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.application.configuration.RequestInterceptor;
import co.netguru.android.inbbbox.db.Storage;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Singleton
@Module
public class ConfigurationModule {

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    public RequestInterceptor providesRequestInterceptor(Storage storage) {
        return new RequestInterceptor(storage);
    }

    @Provides
    OkHttpClient provideOkHttpClient(RequestInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(interceptor)
                .build();
    }
}
