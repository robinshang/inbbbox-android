package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

interface SplashContract {

    interface View extends MvpView {
        void showLoginScreen();

        void showMainScreen();

        void showError(String error);
    }

    interface Presenter extends MvpPresenter<View> {

    }
}
