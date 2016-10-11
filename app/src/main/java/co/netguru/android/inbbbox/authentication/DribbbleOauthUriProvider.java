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
import rx.Observable;
import rx.functions.Func1;

public class DribbbleOauthUriProvider {

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String SCOPE_KEY = "scope";
    private static final String STATE_KEY = "state";
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
                .encodedPath(getStringValue(R.string.dribbleOauthAuthorizeBaseUrl))
                .appendQueryParameter(CLIENT_ID_KEY, getStringValue(R.string.dribbbleClientId))
                .appendQueryParameter(SCOPE_KEY, getStringValue(R.string.dribbleScope))
                .appendQueryParameter(STATE_KEY, stateString)
                .build();
    }

    private String getStringValue(int resId) {
        return resources.getString(resId);
    }
}
