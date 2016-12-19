package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.UserScope;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.di.module.UserModule;
import co.netguru.android.inbbbox.feature.likes.LikesPresenter;
import co.netguru.android.inbbbox.feature.shots.ShotsPresenter;
import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    @Subcomponent.Builder
    interface Builder {
        UserComponent.Builder userModule(UserModule userModule);

        UserComponent build();
    }

    ShotsComponent getShotsComponent();

    LikesFragmentComponent getLikesFragmentComponent();

    ShotDetailsComponent plus(ShotsDetailsModule module);
}
