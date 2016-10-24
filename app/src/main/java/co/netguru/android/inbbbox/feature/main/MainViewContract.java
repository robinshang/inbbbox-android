package co.netguru.android.inbbbox.feature.main;

import android.app.TimePickerDialog;

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
    }

    interface Presenter extends MvpPresenter<View> {

        void toggleButtonClicked(boolean isChecked);

        void performLogout();

        void prepareUserData();

        void timeViewClicked();

        void notificationStatusChanged(boolean status);

        void clearSubscriptions();
    }
}
