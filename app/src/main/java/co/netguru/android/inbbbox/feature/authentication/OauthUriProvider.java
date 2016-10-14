/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;
import android.net.Uri;

import java.util.UUID;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import rx.Observable;

import static co.netguru.android.inbbbox.utils.Constants.OAUTH;

public class OauthUriProvider {

    private Resources resources;

    private String stateString;

    @Inject
    public OauthUriProvider(Resources resources) {

        this.resources = resources;
    }

    public Observable<Uri> getOauthAutorizeUri() {
        initStateString();
        return Observable.just(Uri.parse(getAuthorizeUri()));
    }

    private void initStateString() {
        stateString = UUID.randomUUID().toString();
    }

    private String getAuthorizeUri() {
        return new StringBuilder(OAUTH.OAUTH_BASE_URL + OAUTH.OAUTH_AUTHORIZE_ENDPOINT)
                .append("?")
                .append(OAUTH.CLIENT_ID_KEY)
                .append("=")
                .append(getStringValue(R.string.dribbbleClientId))
                .append("&")
                .append(OAUTH.SCOPE_KEY)
                .append("=")
                .append(getStringValue(R.string.dribbleScope))
                .append("&")
                .append(OAUTH.STATE_KEY)
                .append("=")
                .append(stateString)
                .toString();
    }

    private String getStringValue(int resId) {
        return resources.getString(resId);
    }
}
