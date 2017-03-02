package co.netguru.android.inbbbox.data.session.controllers;

import android.net.Uri;
import android.support.v4.util.Pair;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.data.session.model.Token;
import rx.Observable;
import rx.Single;

import static co.netguru.android.inbbbox.Constants.OAUTH;

@Singleton
public class TokenParametersController {

    @Inject
    public TokenParametersController() {
        //no-op
    }

    public Observable<Pair<String, UUID>> getOauthAuthorizeUrlAndUuidPair() {
        return Observable.fromCallable(() -> {
            UUID uuid = UUID.randomUUID();
            return Pair.create(getAuthorizeUrl(uuid.toString()), uuid);
        });
    }

    private String getAuthorizeUrl(String stateString) {
        return new Uri.Builder()
                .scheme(OAUTH.URI_SCHEME)
                .authority(OAUTH.URI_AUTHORITY)
                .appendEncodedPath(OAUTH.OAUTH_AUTHORIZE_ENDPOINT)
                .encodedQuery(OAUTH.SCOPE_KEY + "=" + OAUTH.INBBBOX_SCOPE) //prevent + sign encoding
                .appendQueryParameter(OAUTH.CLIENT_ID_KEY, BuildConfig.DRIBBBLE_CLIENT_KEY)
                .appendQueryParameter(OAUTH.STATE_KEY, stateString)
                .build().toString();
    }

    public Single<Token> getUserGuestToken() {
        return Single.just(new Token(BuildConfig.DRIBBBLE_CLIENT_TOKEN,
                OAUTH.INBBBOX_DEFAULT_TOKEN_TYPE,
                OAUTH.INBBBOX_GUEST_SCOPE));
    }

    public Single<String> getSignUpUrl() {
        return Single.just(OAUTH.BASE_URL + OAUTH.SIGN_UP_ENDPOINT);
    }
}
