package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import java.util.List;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotFullscreenModule {

    private final List<Shot> allShots;
    private final int previewShotIndex;
    private final ShotDetailsRequest shotDetailsRequest;

    public ShotFullscreenModule(List<Shot> allShots, int previewShotIndex,
                                ShotDetailsRequest shotDetailsRequest) {
        this.previewShotIndex = previewShotIndex;
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
                userShotsController, allShots, previewShotIndex, shotDetailsRequest);
    }
}