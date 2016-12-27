package co.netguru.android.inbbbox.di.module;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.MockAuthorizeApi;
import co.netguru.android.inbbbox.data.MockShotsApi;
import co.netguru.android.inbbbox.data.MockedBucketApi;
import co.netguru.android.inbbbox.data.MockedFollowersApi;
import co.netguru.android.inbbbox.data.MockedLikesApi;
import co.netguru.android.inbbbox.data.MockedUserApi;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.session.AuthorizeApi;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
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

    @Provides
    BucketApi provideBucketApi() {
        return new MockedBucketApi();
    }
}
