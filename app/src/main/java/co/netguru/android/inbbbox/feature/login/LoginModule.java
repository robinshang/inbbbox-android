package co.netguru.android.inbbbox.feature.login;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.controller.TokenParametersController;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @ActivityScope
    TokenParametersController provideOauthUriProvider() {
        return new TokenParametersController();
    }
}