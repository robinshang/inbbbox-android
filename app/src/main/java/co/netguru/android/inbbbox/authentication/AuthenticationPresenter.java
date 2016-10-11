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

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class AuthenticationPresenter
        extends MvpBasePresenter<AuthenticationContract.View>
        implements AuthenticationContract.Presenter {

    private DribbbleOauthUriProvider dribbbleOauthUriProvider;

    @Inject
    public AuthenticationPresenter(DribbbleOauthUriProvider dribbbleOauthUriProvider){
        this.dribbbleOauthUriProvider = dribbbleOauthUriProvider;
    }

    @Override
    public void showLoginView() {
        dribbbleOauthUriProvider
                .getOauthAutorizeUri()
                .doOnError(Throwable :: printStackTrace)
                .doOnNext(uri -> getView().sendActionIntent(uri))
                .subscribe();
    }
}
