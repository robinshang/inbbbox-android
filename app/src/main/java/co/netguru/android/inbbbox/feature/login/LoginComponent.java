package co.netguru.android.inbbbox.feature.login;

import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.api.AuthorizeApiModule;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent extends BaseComponent{

    void inject(LoginActivity loginActivity);

    LoginPresenter getLoginPresenter();

}
