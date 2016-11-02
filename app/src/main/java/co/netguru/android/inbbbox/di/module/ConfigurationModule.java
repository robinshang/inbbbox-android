package co.netguru.android.inbbbox.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.LocalDateTime;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.RequestInterceptor;
import co.netguru.android.inbbbox.api.converter.DateTimeConverter;
import co.netguru.android.inbbbox.data.adapter.AutoGsonAdapterFactory;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.inbbbox.utils.imageloader.GlideImageLoaderManager;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
@Module
public class ConfigurationModule {

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoGsonAdapterFactory.create())
                .registerTypeAdapter(LocalDateTime.class, new DateTimeConverter())
                .create();
    }

    @Provides
    RequestInterceptor providesRequestInterceptor(Storage storage) {
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

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.API.DRIBBLE_BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    ImageLoader provideImageLoaderManager(Context context) {
        return new GlideImageLoaderManager(context);
    }
}
