package co.netguru.android.inbbbox.main;

import android.support.annotation.StringRes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.common.base.ErrorPresenter;
import co.netguru.android.inbbbox.common.base.HttpErrorView;

interface MainViewContract {

    interface View extends MvpView, HttpErrorView {

        void showLogoutMenu();

        void showMainMenu();

        void showLoginActivity();

        void showUserName(String username);

        void showUserPhoto(String url);

        void showTimePickDialog(int startHour, int startMinute);

        void showNotificationTime(String time);

        void changeNotificationStatus(boolean status);

        void changeFollowingStatus(boolean status);

        void changeNewStatus(boolean status);

        void changePopularStatus(boolean status);

        void changeDebutsStatus(boolean status);

        void changeCustomizationStatus(boolean isDetails);

        void changeNightMode(boolean isNightMode);

        void setNightModeStatus(boolean isNightMode);

        void setSettingsListeners();

        void showMessage(@StringRes int message);

        void refreshShotsView();

        void openSignUpPage(String url);

        void showCreateAccountButton();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void toggleButtonChanged(boolean isChecked);

        void performLogout();

        void prepareUserData();

        void timeViewClicked();

        void notificationStatusChanged(boolean status);

        void followingStatusChanged(boolean status);

        void newStatusChanged(boolean status);

        void popularStatusChanged(boolean status);

        void debutsStatusChanged(boolean status);

        void customizationStatusChanged(boolean isDetails);

        void nightModeChanged(boolean isNightMode);

        void onTimePicked(int hour, int minute);

        void onCreateAccountClick();
    }
}
