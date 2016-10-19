package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;
import android.util.Log;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class ApiTokenProvider {

    private AuthorizeApi api;
    private CacheEndpoint cacheEndpoint;

    @Inject
    ApiTokenProvider(AuthorizeApi api, CacheEndpoint cacheEndpoint) {

        this.api = api;
        this.cacheEndpoint = cacheEndpoint;
    }

    public Observable<Token> getToken(String code) {
        return api.getToken(BuildConfig.DRIBBBLE_CLIENT_KEY,
                BuildConfig.DRIBBBLE_CLIENT_SECRET, code)
                .doOnNext(token -> saveTokenToStorage(token));
    }

    private void saveTokenToStorage(Token tokenResponse) {
        cacheEndpoint.save(Constants.Db.TOKEN_KEY, tokenResponse).subscribe();
    }


}
