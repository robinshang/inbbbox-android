/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.authentication;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.utils.ApiErrorParser;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class AuthenticationPresenter
        extends MvpBasePresenter<AuthenticationContract.View>
        implements AuthenticationContract.Presenter {

    private DribbbleOauthUriProvider dribbbleOauthUriProvider;
    private ApiTokenProvider apiTokenProvider;
    private ApiErrorParser apiErrorParser;

    private String code;
    private String oauthErrorMessage;

    @Inject
    public AuthenticationPresenter(DribbbleOauthUriProvider dribbbleOauthUriProvider,
                                   ApiTokenProvider apiTokenProvider,
                                   ApiErrorParser apiErrorParser) {
        this.dribbbleOauthUriProvider = dribbbleOauthUriProvider;
        this.apiTokenProvider = apiTokenProvider;
        this.apiErrorParser = apiErrorParser;
    }

    @Override
    public void showLoginView() {
        dribbbleOauthUriProvider
                .getOauthAutorizeUri()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(uri -> getView().sendActionIntent(uri))
                .subscribe();
    }

    @Override
    public void handleOauthLoginResponse(Uri uri) {
        unpackParamsFromUri(uri);
        selectAuthorizationAction();
    }

    private void selectAuthorizationAction() {
        if (code != null && !code.isEmpty()) {
            apiTokenProvider.getToken(code);
        } else if (oauthErrorMessage != null && oauthErrorMessage.isEmpty()) {
            getView().showApiError(oauthErrorMessage);
        } else {
            getView().showApiError(apiErrorParser.getApiError(Constants.UNDEFINED));
        }
    }

    private void unpackParamsFromUri(Uri uri) {
        code = uri.getQueryParameter(Constants.OAUTH.CODE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }
}
