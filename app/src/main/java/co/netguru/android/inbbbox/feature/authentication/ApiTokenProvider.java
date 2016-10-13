/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;
import android.util.Log;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;

public class ApiTokenProvider {

    private static final String LOG_TAG = "ApiTokenProvider";
    private AuthorizeApi api;
    private Resources resources;

    @Inject
    public ApiTokenProvider(AuthorizeApi api, Resources resources) {

        this.api = api;
        this.resources = resources;
    }

    public Observable<Token> getToken(String code) {
        return api.getToken(resources.getString(R.string.dribbbleClientId),
                resources.getString(R.string.dribbbleClientSecret), code)
                .doOnNext(this::saveTokenToDB);
    }

    private void saveTokenToDB(Token tokenResponse) {
        // TODO: 11.10.2016 caching token
        Log.d(LOG_TAG,"Token received: "+ tokenResponse.toString());
    }
}
