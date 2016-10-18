package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.di.scope.ActivityScope;

@ActivityScope
public class MainActivityPresenter extends MvpBasePresenter<MainViewContract.View>
        implements MainViewContract.Presenter {

    @Inject
    MainActivityPresenter() {

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void toggleButtonClicked(boolean isChecked) {
        if (isChecked) {
            getView().showLogoutMenu();
        } else {
            getView().showMainMenu();
        }
    }
}
