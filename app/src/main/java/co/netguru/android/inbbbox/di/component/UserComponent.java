package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.UserScope;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.di.module.ShotFullscreenModule;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.di.module.UserModule;
import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    @Subcomponent.Builder
    interface Builder {
        UserComponent.Builder userModule(UserModule userModule);

        UserComponent build();
    }

    ShotsComponent plus(ShotsModule shotsModule);

    LikesFragmentComponent plus(LikesFragmentModule module);

    ShotDetailsComponent plus(ShotsDetailsModule module);

    ShotFullscreenComponent plus(ShotFullscreenModule module);
}
