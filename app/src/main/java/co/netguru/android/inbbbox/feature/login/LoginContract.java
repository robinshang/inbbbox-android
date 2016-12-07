package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;

public interface LoginContract {
    interface View extends BaseMvpRestView {

        void openAuthWebViewFragment(String urlString, String stateKey);

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void disableLoginButton();

        void enableLoginButton();
    }

    interface Presenter extends BaseMvpRestPresenter<View> {
        void showLoginView();

        void handleKeysNotMatching();

        void handleWebViewClose();

        void handleOauthCodeReceived(@NonNull String receivedCode);

        void handleUnknownOauthError();

        void handleKnownOauthError(@NonNull String oauthErrorMessage);
    }
}
