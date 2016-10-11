/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.TokenResponse;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;

public class ApiTokenProvider {

    private AuthorizeApi api;

    @Inject
    public ApiTokenProvider(AuthorizeApi api) {

        this.api = api;
    }

    public Observable<TokenResponse> getToken(String code) {
        return api.getToken(Constants.OAUTH.CLIENT_ID_KEY, Constants.OAUTH.CLIENT_SECRET_KEY, code);
    }
}
