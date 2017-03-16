package co.netguru.android.inbbbox.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.ZonedDateTime;

import java.io.File;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.common.analytics.AnalyticsInterceptor;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.gson.AutoGsonAdapterFactory;
import co.netguru.android.inbbbox.common.gson.DateTimeConverter;
import co.netguru.android.inbbbox.common.retrofit.UpdatedRxJavaCallAdapter;
import co.netguru.android.inbbbox.data.cache.CacheRequestInterceptor;
import co.netguru.android.inbbbox.data.cache.CacheResponseInterceptor;
import co.netguru.android.inbbbox.data.session.RequestInterceptor;
import co.netguru.android.inbbbox.data.session.TokenPrefsRepository;
import co.netguru.android.inbbbox.data.session.controllers.LogoutController;
import co.netguru.android.inbbbox.event.RxBus;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ConfigurationModule {

    private static final int HTTP_CACHE_SIZE = 1024 * 1024 * 20;

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoGsonAdapterFactory.create())
                .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
                .create();
    }

    @Provides
    @Singleton
    RequestInterceptor providesRequestInterceptor(TokenPrefsRepository tokenPrefsRepository,
                                                  LogoutController logoutController,
                                                  ErrorController errorController,
                                                  RxBus rxBus, Context context, Cache cache) {
        return new RequestInterceptor(logoutController, errorController,
                tokenPrefsRepository, rxBus, context);
    }

    @Provides
    @Singleton
    CacheRequestInterceptor provideCacheRequestInterceptor() {
        return new CacheRequestInterceptor();
    }

    @Provides
    @Singleton
    CacheResponseInterceptor provideCacheResponseInterceptor() {
        return new CacheResponseInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(RequestInterceptor interceptor,
                                     AnalyticsInterceptor analyticsInterceptor,
                                     Cache cache, CacheRequestInterceptor cacheRequestInterceptor,
                                     CacheResponseInterceptor cacheResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor(interceptor)
                .addInterceptor(analyticsInterceptor)
                .addInterceptor(cacheRequestInterceptor)
                .addNetworkInterceptor(cacheResponseInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Context context) {
        File cacheDirectory = new File(context.getCacheDir(), "http");
        return new Cache(cacheDirectory, HTTP_CACHE_SIZE);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                // TODO: 15.12.2016 change to regular call adapter as noted in UpdatedRxJavaCallAdapter
                .addCallAdapterFactory(UpdatedRxJavaCallAdapter.create())
                .baseUrl(Constants.API.DRIBBLE_BASE_URL)
                .client(okHttpClient)
                .build();
    }
}