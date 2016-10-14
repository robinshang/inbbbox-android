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
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import rx.functions.Func1;

public class ApiTokenProvider {

    private static final String LOG_TAG = "ApiTokenProvider";
    private AuthorizeApi api;
    private CacheEndpoint cacheEndpoint;
    private Resources resources;

    @Inject
    ApiTokenProvider(AuthorizeApi api, CacheEndpoint cacheEndpoint, Resources resources) {

        this.api = api;
        this.cacheEndpoint = cacheEndpoint;
        this.resources = resources;
    }

    public Observable getToken(String code) {
        return api.getToken(resources.getString(R.string.dribbbleClientId),
                resources.getString(R.string.dribbbleClientSecret), code)
                .flatMap((Func1<Token, Observable<?>>) this::saveTokenToStorage);
    }

    private Observable saveTokenToStorage(Token tokenResponse) {
        Log.d(LOG_TAG,"Token received: "+ tokenResponse.toString());
        return cacheEndpoint.save(Constants.Db.TOKEN_KEY, tokenResponse);
    }
}
