package co.netguru.android.inbbbox.feature.user.info;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface UserInfoComponent {

    void inject(UserInfoFragment fragment);

    UserInfoPresenter getPresenter();
}
