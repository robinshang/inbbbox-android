package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.di.module.CacheEndpointModule;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.feature.login.LoginPresenter;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {LoginModule.class, CacheEndpointModule.class})
public interface LoginComponent extends BaseComponent{

    void inject(LoginActivity loginActivity);

    LoginPresenter getLoginPresenter();

}
