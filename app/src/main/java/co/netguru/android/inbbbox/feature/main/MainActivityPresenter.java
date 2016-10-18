package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;

@SuppressWarnings("ConstantConditions")
@ActivityScope
public class MainActivityPresenter extends MvpBasePresenter<MainViewContract.View>
        implements Presenter {

    @Inject
    MainActivityPresenter() {

    }

    @Override
    public void toggleButtonClicked(boolean isChecked) {
        if (isChecked) {
            getView().showLogoutMenu();
        } else {
            getView().showMainMenu();
        }
    }

    @Override
    public void performLogout() {
        // TODO: 18.10.2016 Clear user shared preferences
        getView().showLoginActivity();
    }
}
