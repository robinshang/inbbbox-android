package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.controler.OauthUrlController;
import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class LoginModule {

    @Provides
    OauthUrlController provideOauthUriProvider() {
        return new OauthUrlController();
    }
}
