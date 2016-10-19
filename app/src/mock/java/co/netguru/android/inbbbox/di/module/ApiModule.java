package co.netguru.android.inbbbox.di.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.api.MockAuthorizeApi;
import co.netguru.android.inbbbox.data.api.MockedUserApi;
import co.netguru.android.inbbbox.data.api.UserApi;
import co.netguru.android.inbbbox.utils.Constants;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Singleton
    @Provides
    AuthorizeApi provideAuthorizeApi(OkHttpClient okHttpClient, Gson gson) {
        return new MockAuthorizeApi();
    }

    @Singleton
    @Provides
    UserApi provideAuthenticatedUserApi(OkHttpClient okHttpClient, Gson gson) {
        return new MockedUserApi();
    }
}