package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

interface MainViewContract {

    interface View extends MvpView {

        void showLogoutMenu();

        void showMainMenu();

        void showLoginActivity();
    }

    interface Presenter extends MvpPresenter<View> {

        void toggleButtonClicked(boolean isChecked);

        void performLogout();
    }
}
