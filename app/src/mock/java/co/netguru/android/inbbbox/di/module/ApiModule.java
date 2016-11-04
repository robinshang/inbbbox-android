package co.netguru.android.inbbbox.di.module;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.api.FollowersApi;
import co.netguru.android.inbbbox.data.api.LikesApi;
import co.netguru.android.inbbbox.data.api.MockAuthorizeApi;
import co.netguru.android.inbbbox.data.api.MockShotsApi;
import co.netguru.android.inbbbox.data.api.MockedFollowersApi;
import co.netguru.android.inbbbox.data.api.MockedLikesApi;
import co.netguru.android.inbbbox.data.api.MockedUserApi;
import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.api.UserApi;
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
