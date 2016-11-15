package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;
import android.support.v4.util.Pair;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.R;
import rx.Observable;

import static co.netguru.android.inbbbox.Constants.OAUTH;

@Singleton
public class OauthUrlController {

    private Resources resources;

    @Inject
    public OauthUrlController(Resources resources) {

        this.resources = resources;
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
                getStringValue(R.string.dribbbleScope) +
                "&" +
                OAUTH.STATE_KEY +
                "=" +
                stateString;
    }

    private String getStringValue(Integer resId) {
        return resources.getString(resId);
    }
}
