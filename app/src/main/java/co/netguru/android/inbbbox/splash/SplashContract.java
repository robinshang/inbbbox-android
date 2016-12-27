package co.netguru.android.inbbbox.splash;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.common.base.ErrorPresenter;
import co.netguru.android.inbbbox.common.base.HttpErrorView;

interface SplashContract {

    interface View extends MvpView, HttpErrorView {
        void showLoginScreen();

        void showMainScreen();

        void initializeOnlineUserMode();

        void setDefaultNightMode(boolean isNightMode);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void initializeDefaultNightMode();
    }
}
