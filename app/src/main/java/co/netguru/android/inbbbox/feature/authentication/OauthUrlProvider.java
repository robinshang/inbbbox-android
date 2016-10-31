package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;

import java.util.UUID;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.R;
import rx.Observable;

import static co.netguru.android.inbbbox.utils.Constants.OAUTH;

public class OauthUrlProvider {

    private Resources resources;

    private String stateString;

    @Inject
    public OauthUrlProvider(Resources resources) {

        this.resources = resources;
    }

    public Observable<String> getOauthAuthorizeUrlString() {
        initStateString();
        return Observable.just(getAuthorizeUrl());
    }

    private void initStateString() {
        stateString = UUID.randomUUID().toString();
    }

    private String getAuthorizeUrl() {
        return OAUTH.BASE_URL + OAUTH.OAUTH_AUTHORIZE_ENDPOINT +
                "?" +
                OAUTH.CLIENT_ID_KEY +
                "=" +
                BuildConfig.DRIBBBLE_CLIENT_KEY +
                "&" +
                OAUTH.SCOPE_KEY +
                "=" +
                getStringValue(R.string.dribbleScope) +
                "&" +
                OAUTH.STATE_KEY +
                "=" +
                stateString;
    }

    private String getStringValue(Integer resId) {
        return resources.getString(resId);
    }
}
