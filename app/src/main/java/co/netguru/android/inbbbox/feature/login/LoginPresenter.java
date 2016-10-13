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

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.utils.ApiErrorParser;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public class LoginPresenter
        extends MvpBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private CompositeSubscription subscription = new CompositeSubscription();
    private OauthUriProvider uriProvider;
    private ApiTokenProvider apiTokenProvider;
    private ApiErrorParser apiErrorParser;

    private String code;
    private String oauthErrorMessage;
    private String currentState;

    @Inject
    public LoginPresenter(OauthUriProvider dribbbleOauthUriProvider,
                          ApiTokenProvider apiTokenProvider,
                          ApiErrorParser apiErrorParser) {
        this.uriProvider = dribbbleOauthUriProvider;
        this.apiTokenProvider = apiTokenProvider;
        this.apiErrorParser = apiErrorParser;
    }

    @Override
    public void showLoginView() {
        uriProvider
                .getOauthAutorizeUri()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(uri -> prepareAuthorization(uri))
                .subscribe();
    }

    private void prepareAuthorization(Uri uri) {
        getView().sendActionIntent(uri);
    }

    @Override
    public void handleOauthLoginResponse(Uri uri) {
        if (uri != null) {
            unpackParamsFromUri(uri);
            selectAuthorizationAction();
        }
    }

    private void selectAuthorizationAction() {
        // TODO: 13.10.2016 sent state check
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
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Token>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(Token token) {
                                showMainScreen();
                            }
                        })
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
        currentState = uri.getQueryParameter(Constants.OAUTH.STATE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }
}
