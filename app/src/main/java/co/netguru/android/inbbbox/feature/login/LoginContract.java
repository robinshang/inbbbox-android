package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginContract {
    interface View extends MvpView{

        void handleOauthUrl(String uriString);

        void showApiError(String oauthErrorMessage);

        void showNextScreen();

        void closeLoginDialog();
    }

    interface Presenter extends MvpPresenter<View>{
        void showLoginView();

        void handleOauthLoginResponse(Uri uri);
    }
}
