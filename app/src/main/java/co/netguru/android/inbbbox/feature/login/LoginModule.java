package co.netguru.android.inbbbox.feature.login;

import android.content.res.Resources;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @ActivityScope
    OauthUriProvider provideOauthUriProvider(Resources resources) {
        return new OauthUriProvider(resources);
    }

}
