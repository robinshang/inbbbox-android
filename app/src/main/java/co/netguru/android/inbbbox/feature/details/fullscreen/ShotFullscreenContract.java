package co.netguru.android.inbbbox.feature.details.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;

public interface ShotFullscreenContract {

    interface View extends MvpView {
        void previewShot(Shot shot, List<Shot> allShots);
    }

    interface Presenter extends MvpPresenter<View> {
        void onViewCreated(Shot shot, List<Shot> allShots);
    }
}
