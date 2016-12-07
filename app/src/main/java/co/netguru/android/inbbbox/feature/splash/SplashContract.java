package co.netguru.android.inbbbox.feature.splash;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;

interface SplashContract {

    interface View extends BaseMvpRestView {
        void showLoginScreen();

        void showMainScreen();
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

    }
}
