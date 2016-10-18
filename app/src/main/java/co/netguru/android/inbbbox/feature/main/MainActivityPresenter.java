package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

@ActivityScope
public final class MainActivityPresenter extends MvpNullObjectBasePresenter<MainViewContract.View>
        implements Presenter {

    // TODO: 18.10.2016 Get user data from db
    private static final String MOCK_USER_NAME = "Some User";
    private static final String MOCK_USER_EMAIL = "some.user@gmail.com";
    private static final String MOCK_USER_PHOTO = "";

    private final LocalTimeFormatter localTimeFormatter;

    @Inject
    MainActivityPresenter(LocalTimeFormatter localTimeFormatter) {
        this.localTimeFormatter = localTimeFormatter;
    }

    @Override
    public void toggleButtonClicked(boolean isChecked) {
        if (isChecked) {
            getView().showLogoutMenu();
            getView().showUserName(MOCK_USER_NAME);
        } else {
            getView().showMainMenu();
            getView().showUserName(MOCK_USER_EMAIL);
        }
    }

    @Override
    public void performLogout() {
        // TODO: 18.10.2016 Clear user shared preferences
        getView().showLoginActivity();
    }

    @Override
    public void prepareUserData() {
        getView().showUserName(MOCK_USER_EMAIL);
        getView().showUserPhoto(MOCK_USER_PHOTO);
        getView().showChangedTime(localTimeFormatter.getFormattedCurrentTime());
    }

    @Override
    public void timeViewClicked() {
        getView().showTimePickDialog(localTimeFormatter.getCurrentHour(),
                localTimeFormatter.getCurrentMinute(),
                (view, hourOfDay, minute) ->
                        getView().showChangedTime(localTimeFormatter.getFormattedTime(hourOfDay, minute))
                );
    }
}
