package co.netguru.android.inbbbox.feature.user.info.singleuser;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = UserInfoModule.class)
public interface UserInfoComponent {

    void inject(UserInfoFragment userInfoFragment);

    UserInfoPresenter getPresenter();
}
