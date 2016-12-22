package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.di.module.usermodule.UserModeType;
import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;

public interface LoginContract {
    interface View extends MvpView, HttpErrorView {

        void openAuthWebViewFragment(String urlString, String stateKey);

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void disableLoginButton();

        void enableLoginButton();

        void showGuestModeLoginButton();

        void initializeUserMode(UserModeType guestUserMode);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void showLoginView();

        void handleKeysNotMatching();

        void handleWebViewClose();

        void handleOauthCodeReceived(@NonNull String receivedCode);

        void handleUnknownOauthError();

        void handleKnownOauthError(@NonNull String oauthErrorMessage);

        void checkGuestMode();

        void loginWithGuestClicked();
    }
}
