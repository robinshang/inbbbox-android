package co.netguru.android.inbbbox.app.usercomponent;

import co.netguru.android.inbbbox.feature.follower.FollowersFragmentComponent;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.feature.like.LikesFragmentComponent;
import co.netguru.android.inbbbox.feature.shot.ShotsComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenComponent;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenModule;
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
