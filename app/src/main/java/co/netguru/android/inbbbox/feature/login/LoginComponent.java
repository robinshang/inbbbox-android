package co.netguru.android.inbbbox.feature.login;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.commons.di.BaseComponent;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.db.CacheEndpointModule;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {LoginModule.class, CacheEndpointModule.class})
public interface LoginComponent extends BaseComponent{

    void inject(LoginActivity loginActivity);

    LoginPresenter getLoginPresenter();

}
