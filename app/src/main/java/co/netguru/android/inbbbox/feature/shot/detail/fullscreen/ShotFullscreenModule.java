package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import java.util.List;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controller.BucketsController;
import co.netguru.android.inbbbox.controller.ShotsController;
import co.netguru.android.inbbbox.controller.UserShotsController;
import co.netguru.android.inbbbox.controller.likescontroller.LikeShotController;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullScreenPresenter;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenAdapter;
import co.netguru.android.inbbbox.model.ui.Shot;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotFullscreenModule {

    private final Shot shot;
    private final List<Shot> allShots;
    private final ShotDetailsRequest shotDetailsRequest;

    public ShotFullscreenModule(Shot shot, List<Shot> allShots, ShotDetailsRequest shotDetailsRequest) {
        this.shot = shot;
        this.allShots = allShots;
        this.shotDetailsRequest = shotDetailsRequest;
    }

    @Provides
    @FragmentScope
    ShotFullscreenAdapter provideShotsAdapter() {
        return new ShotFullscreenAdapter();
    }

    @Provides
    @FragmentScope
    ShotFullScreenPresenter provideShotDetailsPresenter(ShotsController shotsController,
                                                        LikeShotController likeShotController,
                                                        BucketsController bucketsController,
                                                        UserShotsController userShotsController) {
        return new ShotFullScreenPresenter(shotsController, likeShotController, bucketsController,
                userShotsController, shot, allShots, shotDetailsRequest);
    }
}