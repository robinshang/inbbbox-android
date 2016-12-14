package co.netguru.android.inbbbox.feature.details.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.model.ui.Shot;

public interface ShotFullscreenContract {

    interface View extends MvpView {
        void previewShot(Shot shot);
    }

    interface Presenter extends MvpPresenter<View> {
        void onViewCreated(Shot shot);
    }
}
