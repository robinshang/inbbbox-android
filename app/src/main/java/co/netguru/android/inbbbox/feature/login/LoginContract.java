package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginContract {
    interface View extends MvpView {

        void openAuthWebViewFragment(String urlString, String stateKey);

        void showApiError(String oauthErrorMessage);

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void disableLoginButton();

        void enableLoginButton();

        void showGuestModeLoginButton();
    }

    interface Presenter extends MvpPresenter<View> {
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
