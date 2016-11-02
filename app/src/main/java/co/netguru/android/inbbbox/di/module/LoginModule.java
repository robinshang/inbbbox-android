package co.netguru.android.inbbbox.di.module;

import android.content.res.Resources;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.authentication.OauthUrlProvider;
import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class LoginModule {

    @Provides
    OauthUrlProvider provideOauthUriProvider(Resources resources) {
        return new OauthUrlProvider(resources);
    }
}
