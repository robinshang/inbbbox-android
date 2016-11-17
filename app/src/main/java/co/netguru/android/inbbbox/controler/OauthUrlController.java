package co.netguru.android.inbbbox.controler;

import android.support.v4.util.Pair;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.BuildConfig;
import rx.Observable;

import static co.netguru.android.inbbbox.Constants.OAUTH;

@Singleton
public class OauthUrlController {

    @Inject
    public OauthUrlController() {
    }

    public Observable<Pair<String, UUID>> getOauthAuthorizeUrlAndUuidPair() {
        return Observable.fromCallable(() -> {
            UUID uuid = UUID.randomUUID();
            return Pair.create(getAuthorizeUrl(uuid.toString()), uuid);
        });
    }

    private String getAuthorizeUrl(String stateString) {
        return OAUTH.BASE_URL + OAUTH.OAUTH_AUTHORIZE_ENDPOINT +
                "?" +
                OAUTH.CLIENT_ID_KEY +
                "=" +
                BuildConfig.DRIBBBLE_CLIENT_KEY +
                "&" +
                OAUTH.SCOPE_KEY +
                "=" +
                OAUTH.INBBBOX_SCOPE +
                "&" +
                OAUTH.STATE_KEY +
                "=" +
                stateString;
    }

}
