package co.netguru.android.inbbbox.feature.details.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotFullScreenPresenter extends MvpNullObjectBasePresenter<ShotFullscreenContract.View>
        implements ShotFullscreenContract.Presenter {

    @Inject
    public ShotFullScreenPresenter() {

    }

    @Override
    public void onViewCreated(Shot shot) {
        getView().previewShot(shot);
    }
}
