package co.netguru.android.inbbbox.feature.main;

import android.app.TimePickerDialog;
import android.support.annotation.Nullable;
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

        void changeStreamSourceStatus(boolean isFollowing, boolean isNew, boolean isPopular, boolean isDebuts);

        void changeCustomizationStatus(boolean isDetails);

        void showMessage(@StringRes int message);
    }

    interface Presenter extends MvpPresenter<View> {

        void toggleButtonClicked(boolean isChecked);

        void performLogout();

        void prepareUserData();

        void timeViewClicked();

        void notificationStatusChanged(boolean status);

        void streamSourceStatusChanged(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                       @Nullable Boolean isPopular, @Nullable Boolean isDebuts);

        void customizationStatusChanged(boolean isDetails);
    }
}
