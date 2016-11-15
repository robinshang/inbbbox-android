package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginContract {
    interface View extends MvpView {

        void handleOauthUrlAndUuid(String urlString, String stateKey);

        void showApiError(String oauthErrorMessage);

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void disableLoginButton();

        void enableLoginButton();
    }

    interface Presenter extends MvpPresenter<View> {
        void showLoginView();

        void handleOauthLoginResponse(Uri uri);

        void handleKeysNotMatching();

        void handleWebViewClose();
    }
}
