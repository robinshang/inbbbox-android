package co.netguru.android.inbbbox.feature.user.info;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = UserInfoModule.class)
public interface UserInfoComponent {

    void inject(UserInfoFragment fragment);

    UserInfoPresenter getPresenter();
}
