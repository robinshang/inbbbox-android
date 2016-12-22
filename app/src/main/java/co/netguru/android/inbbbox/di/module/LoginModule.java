package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.controler.TokenParametersController;
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