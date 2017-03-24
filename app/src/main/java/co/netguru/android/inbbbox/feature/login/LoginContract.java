package co.netguru.android.inbbbox.feature.login;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface LoginContract {
    interface View extends MvpView, HttpErrorView {

        void openAuthWebViewFragment(String urlString, String stateKey);

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void disableLoginButton();

        void enableLoginButton();

        void showGuestModeLoginButton();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void showLoginView();

        void checkGuestMode();

        void loginWithGuestClicked();
    }
}
