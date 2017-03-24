package co.netguru.android.inbbbox.app.usercomponent;

import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.GuestModeBucketsRepository;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsControllerApi;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsControllerGuest;
import co.netguru.android.inbbbox.data.cache.CacheValidator;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamApi;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamControllerApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersControllerApi;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersControllerGuest;
import co.netguru.android.inbbbox.data.follower.controllers.GuestModeFollowersRepository;
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.like.controllers.GuestModeLikesRepository;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerApi;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerGuest;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    private UserModeType mode;

    public UserModule(UserModeType mode) {

        this.mode = mode;
    }

    @Provides
    @UserScope
    LikeShotController likeShotController(LikesApi likesApi, GuestModeLikesRepository guestModeLikesRepository) {
        LikeShotController likeShotControllerApi;
        if (mode == UserModeType.GUEST_USER_MODE) {
            likeShotControllerApi = new LikeShotControllerGuest(guestModeLikesRepository, likesApi);
        } else {
            likeShotControllerApi = new LikeShotControllerApi(likesApi);
        }
        return likeShotControllerApi;
    }

    @UserScope
    @Provides
    FollowersController provideFollowersController(FollowersApi followersApi, ShotsApi shotsApi,
                                                   GuestModeFollowersRepository guestModeFollowersRepository) {
        if (mode == UserModeType.GUEST_USER_MODE) {
            return new FollowersControllerGuest(guestModeFollowersRepository, followersApi, shotsApi);
        }

        return new FollowersControllerApi(followersApi, shotsApi);
    }

    @UserScope
    @Provides
    BucketsController provideBucketsController(UserApi userApi, BucketApi bucketApi,
                                               GuestModeBucketsRepository guestModeBucketsRepository,
                                               UserController userController,
                                               CacheValidator cacheValidator) {
        if (mode == UserModeType.GUEST_USER_MODE) {
            return new BucketsControllerGuest(guestModeBucketsRepository, userApi, bucketApi,
                    userController, cacheValidator);
        }

        return new BucketsControllerApi(userApi, bucketApi, userController, cacheValidator);
    }

    @UserScope
    @Provides
    TeamController provideTeamController(TeamApi teamApi) {
        return new TeamControllerApi(teamApi);
    }
}
