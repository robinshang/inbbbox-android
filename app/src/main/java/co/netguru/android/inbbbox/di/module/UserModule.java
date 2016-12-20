package co.netguru.android.inbbbox.di.module;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.likescontroller.LikeShotControllerApi;
import co.netguru.android.inbbbox.controler.likescontroller.LikeShotControllerGuest;
import co.netguru.android.inbbbox.di.UserScope;
import co.netguru.android.inbbbox.enumeration.UserModeType;
import co.netguru.android.inbbbox.localrepository.GuestModeRepository;
import dagger.Module;
import dagger.Provides;

@Module
@UserScope
public class UserModule {

    private UserModeType mode;

    public UserModule(UserModeType mode) {

        this.mode = mode;
    }

    @Provides
    @UserScope
    LikeShotController likeShotController(LikesApi likesApi, GuestModeRepository guestModeRepository) {
        LikeShotController likeShotControllerApi;
        if (mode.equals(UserModeType.GUEST_USER_MODE)) {
            likeShotControllerApi = new LikeShotControllerGuest(guestModeRepository, likesApi);
        } else {
            likeShotControllerApi = new LikeShotControllerApi(likesApi);
        }
        return likeShotControllerApi;
    }
}
