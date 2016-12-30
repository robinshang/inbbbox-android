package co.netguru.android.inbbbox.app;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.MockedBucketApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.MockedUserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.MockedFollowersApi;
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.like.MockedLikesApi;
import co.netguru.android.inbbbox.data.session.AuthorizeApi;
import co.netguru.android.inbbbox.data.session.MockedAuthorizeApi;
import co.netguru.android.inbbbox.data.shot.MockedShotsApi;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApiModule {

    @Provides
    AuthorizeApi provideAuthorizeApi() {
        return new MockedAuthorizeApi();
    }

    @Provides
    UserApi provideAuthenticatedUserApi() {
        return new MockedUserApi();
    }

    @Provides
    ShotsApi provideShotsApi() {
        return new MockedShotsApi();
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
