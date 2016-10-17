/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginContract {
    interface View extends MvpView{

        void sendActionIntent(Uri uri);

        void showApiError(String oauthErrorMessage);

        void showNextScreen();

    }

    interface Presenter extends MvpPresenter<View>{
        void showLoginView();

        void handleOauthLoginResponse(Uri uri);
    }
}
