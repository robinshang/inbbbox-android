package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface SplashContract {

    interface View extends MvpView, HttpErrorView {
        void showLoginScreen();

        void showMainScreen();

        void setDefaultNightMode(boolean isNightMode);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void initializeDefaultNightMode();
    }
}
