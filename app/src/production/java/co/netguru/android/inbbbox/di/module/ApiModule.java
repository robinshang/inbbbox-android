package co.netguru.android.inbbbox.di.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.api.BucketApi;
import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.api.ShotDetailsApi;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.api.UserApi;
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
    AuthorizeApi provideAuthorizeApi(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.OAUTH.BASE_URL)
                .client(okHttpClient)
                .build()
                .create(AuthorizeApi.class);
    }

    @Singleton
    @Provides
    UserApi provideAuthenticatedUserApi(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Singleton
    @Provides
    ShotsApi provideShotsApi(Retrofit retrofit) {
        return retrofit.create(ShotsApi.class);
    }

    @Singleton
    @Provides
    LikesApi provideLikesApi(Retrofit retrofit) {
        return retrofit.create(LikesApi.class);
    }

    @Singleton
    @Provides
    FollowersApi provideFollowersApi(Retrofit retrofit) {
        return retrofit.create(FollowersApi.class);
    }

    @Singleton
    @Provides
    BucketApi providesBucketApi(Retrofit retrofit) {
        return retrofit.create(BucketApi.class);
    }

    @Singleton
    @Provides
    ShotDetailsApi provideShotDetailsApi(Retrofit retrofit) {
        return retrofit.create(ShotDetailsApi.class);
    }

    @Singleton
    @Provides
    ShotCommentsApi provideShotCommentsApi(Retrofit retrofit) {
        return retrofit.create(ShotCommentsApi.class);
    }
}
