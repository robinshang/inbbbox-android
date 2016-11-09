package co.netguru.android.inbbbox.di.module;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.api.MockAuthorizeApi;
import co.netguru.android.inbbbox.api.MockShotsApi;
import co.netguru.android.inbbbox.api.MockedFollowersApi;
import co.netguru.android.inbbbox.api.MockedLikesApi;
import co.netguru.android.inbbbox.api.MockedUserApi;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.api.UserApi;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApiModule {

    @Provides
    AuthorizeApi provideAuthorizeApi() {
        return new MockAuthorizeApi();
    }

    @Provides
    UserApi provideAuthenticatedUserApi() {
        return new MockedUserApi();
    }

    @Provides
    ShotsApi provideShotsApi() {
        return new MockShotsApi();
    }

    @Provides
    LikesApi provideLikesApi() {
        return new MockedLikesApi();
    }

    @Provides
    FollowersApi provideFollowersApi() {
        return new MockedFollowersApi();
    }
}
