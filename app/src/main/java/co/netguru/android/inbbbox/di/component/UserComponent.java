package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.UserScope;
import co.netguru.android.inbbbox.di.module.ShotFullscreenModule;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.di.module.usermodule.UserModule;
import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    ShotsComponent getShotsComponent();

    LikesFragmentComponent getLikesFragmentComponent();

    ShotDetailsComponent plus(ShotsDetailsModule module);

    ShotFullscreenComponent plus(ShotFullscreenModule module);

    FollowersFragmentComponent plusFollowersFragmentComponent();

    FollowerDetailsFragmentComponent plusFollowersDetailsFragmentComponent();

    @Subcomponent.Builder
    interface Builder {
        UserComponent.Builder userModule(UserModule userModule);

        UserComponent build();
    }
}
