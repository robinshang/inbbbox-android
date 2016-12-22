package co.netguru.android.inbbbox.app.usermodule;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.controller.followers.FollowersController;
import co.netguru.android.inbbbox.controller.followers.FollowersControllerApi;
import co.netguru.android.inbbbox.controller.followers.FollowersControllerGuest;
import co.netguru.android.inbbbox.controller.likescontroller.LikeShotController;
import co.netguru.android.inbbbox.controller.likescontroller.LikeShotControllerApi;
import co.netguru.android.inbbbox.controller.likescontroller.LikeShotControllerGuest;
import co.netguru.android.inbbbox.localrepository.database.GuestModeFollowersRepository;
import co.netguru.android.inbbbox.localrepository.database.GuestModeLikesRepository;
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
