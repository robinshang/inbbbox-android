package co.netguru.android.inbbbox.feature.main;

import android.app.TimePickerDialog;
import android.support.annotation.StringRes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

interface MainViewContract {

    interface View extends MvpView {

        void showLogoutMenu();

        void showMainMenu();

        void showLoginActivity();

        void showUserName(String username);

        void showUserPhoto(String url);

        void showTimePickDialog(int startHour, int startMinute, TimePickerDialog.OnTimeSetListener onTimeSetListener);

        void showNotificationTime(String time);

        void changeNotificationStatus(boolean status);

        void changeFollowingStatus(boolean status);

        void changeNewStatus(boolean status);

        void changePopularStatus(boolean status);

        void changeDebutsStatus(boolean status);

        void changeCustomizationStatus(boolean isDetails);

        void showMessage(@StringRes int message);

        void refreshShotsView();
    }

    interface Presenter extends MvpPresenter<View> {

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
    }
}
