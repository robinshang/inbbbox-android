package co.netguru.android.inbbbox.feature.login;

import android.content.res.Resources;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.utils.ApiErrorParser;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @FragmentScope
    OauthUriProvider provideOauthUriProvider(Resources resources) {
        return new OauthUriProvider(resources);
    }

    @FragmentScope
    @Provides
    LoginPresenter provideLoginPresenter(OauthUriProvider dribbbleOauthUriProvider,
                                         ApiTokenProvider apiTokenProvider,
                                         ApiErrorParser apiErrorParser) {
        return new LoginPresenter(dribbbleOauthUriProvider, apiTokenProvider, apiErrorParser);
    }
}
