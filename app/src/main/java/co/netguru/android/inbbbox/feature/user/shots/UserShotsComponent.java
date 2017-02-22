package co.netguru.android.inbbbox.feature.user.shots;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = UserShotsModule.class)
public interface UserShotsComponent {

    void inject(UserShotsFragment fragment);

    UserShotsPresenter getPresenter();
}
