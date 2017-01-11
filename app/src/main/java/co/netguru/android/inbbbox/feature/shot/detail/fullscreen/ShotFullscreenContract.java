package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotFullscreenContract {

    interface View extends MvpView {
        void previewShots(List<Shot> allShots, int previewShotIndex);

        void showMoreItems(List<Shot> shots);

        void close();
    }

    interface Presenter extends MvpPresenter<View> {
        void onRequestMoreData();

        void onBackArrowPressed();
    }
}