/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.authentication;

import android.content.res.Resources;
import android.net.Uri;

import java.util.UUID;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;

import static co.netguru.android.inbbbox.utils.Constants.OAUTH;

public class DribbbleOauthUriProvider {

    private Resources resources;

    private String stateString;

    @Inject
    public DribbbleOauthUriProvider(Resources resources) {

        this.resources = resources;
    }

    public Observable<Uri> getOauthAutorizeUri() {
        initStateString();
        return Observable.just(getAuthorizeUri());
    }

    private void initStateString() {
        stateString = UUID.randomUUID().toString();
    }

    private Uri getAuthorizeUri() {
        return new Uri.Builder()
                .encodedPath(OAUTH.AUTHORIZE_URL)
                .appendQueryParameter(OAUTH.CLIENT_ID_KEY, getStringValue(R.string.dribbbleClientId))
                .appendQueryParameter(OAUTH.SCOPE_KEY, getStringValue(R.string.dribbleScope))
                .appendQueryParameter(OAUTH.STATE_KEY, stateString)
                .build();
    }

    private String getStringValue(int resId) {
        return resources.getString(resId);
    }
}
