package co.netguru.android.inbbbox.app.usermodule;

import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersControllerApi;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersControllerGuest;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerApi;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotControllerGuest;
import co.netguru.android.inbbbox.data.follower.controllers.GuestModeFollowersRepository;
import co.netguru.android.inbbbox.data.like.controllers.GuestModeLikesRepository;
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
    FollowersController provideFollowersController(FollowersApi followersApi,
                                                   GuestModeFollowersRepository guestModeFollowersRepository) {
        if (mode == UserModeType.GUEST_USER_MODE) {
            return new FollowersControllerGuest(guestModeFollowersRepository, followersApi);
        }

        return new FollowersControllerApi(followersApi);
    }
}
