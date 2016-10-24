package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface SplashContract {

    interface View extends MvpView {
        void openLoginScreen();

        void openMainScreen();

    }

    interface Presenter extends MvpPresenter<View> {

    }
}
