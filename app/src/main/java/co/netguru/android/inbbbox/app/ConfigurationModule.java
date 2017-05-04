package co.netguru.android.inbbbox.app;

import android.content.Context;
import android.os.Vibrator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Collections;

import javax.inject.Singleton;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
import timber.log.Timber;

@Module
public class ConfigurationModule {

    private static final int HTTP_CACHE_SIZE = 1024 * 1024 * 20;
    private static final String HTTP_CACHE_DIRECTORY = "cache";

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
                                     CacheResponseInterceptor cacheResponseInterceptor,
                                     X509TrustManager trustManager) {
        NoSSLv3Factory tlsSocketFactory = null;
        try {
            tlsSocketFactory = new NoSSLv3Factory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Timber.e(e);
        }

        return new OkHttpClient.Builder()
                 .sslSocketFactory(tlsSocketFactory, trustManager)
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
        File cacheDirectory = new File(context.getCacheDir(), HTTP_CACHE_DIRECTORY);
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

    @Provides
    @Singleton
    Vibrator provideVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Provides
    @Singleton
    X509TrustManager provideTrustManager() {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                return null;
            }
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyStoreException e) {
            Timber.e(e);
            return null;
        }
    }
}