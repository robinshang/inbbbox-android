/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.utils.ApiErrorParser;
import co.netguru.android.inbbbox.utils.Constants;
import rx.subscriptions.CompositeSubscription;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class LoginPresenter
        extends MvpBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private CompositeSubscription subscription = new CompositeSubscription();
    private OauthUriProvider dribbbleOauthUriProvider;
    private ApiTokenProvider apiTokenProvider;
    private ApiErrorParser apiErrorParser;

    private String code;
    private String oauthErrorMessage;

    @Inject
    public LoginPresenter(OauthUriProvider dribbbleOauthUriProvider,
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
            getToken();
        } else if (oauthErrorMessage != null && oauthErrorMessage.isEmpty()) {
            getView().showApiError(oauthErrorMessage);
        } else {
            getView().showApiError(apiErrorParser.getApiError(Constants.UNDEFINED));
        }
    }

    private void getToken() {
        subscription.add(
                apiTokenProvider.getToken(code)
                        .compose(androidIO())
                        .doOnError(throwable -> handleError(throwable))
                        .doOnNext(userData -> showMainScreen())
                        .subscribe()
        );
    }

    private void showMainScreen() {
        getView().showNextScreen();
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        getView().showApiError(apiErrorParser.getApiError(throwable));
    }

    private void unpackParamsFromUri(Uri uri) {
        code = uri.getQueryParameter(Constants.OAUTH.CODE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }
}
