package co.netguru.android.inbbbox.feature.login;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.commons.di.BaseComponent;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent extends BaseComponent {

    void inject(LoginActivity loginActivity);

    LoginPresenter getLoginPresenter();

}
