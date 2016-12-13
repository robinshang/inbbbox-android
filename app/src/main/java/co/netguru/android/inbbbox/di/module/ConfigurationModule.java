package co.netguru.android.inbbbox.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.LocalDateTime;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.DateTimeConverter;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.api.RequestInterceptor;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.LogoutController;
import co.netguru.android.inbbbox.controler.likescontroller.LikeShotControllerGuest;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
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
    RequestInterceptor providesRequestInterceptor(TokenPrefsRepository tokenPrefsRepository,
                                                  LogoutController logoutController,
                                                  ErrorController errorController,
                                                  RxBus rxBus) {
        return new RequestInterceptor(logoutController, errorController,
                tokenPrefsRepository, rxBus);
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
    LikeShotController provideLikeShotController(GuestModeRepository guestModeRepository, LikesApi likesApi) {
        return new LikeShotControllerGuest(guestModeRepository, likesApi);
    }
}
