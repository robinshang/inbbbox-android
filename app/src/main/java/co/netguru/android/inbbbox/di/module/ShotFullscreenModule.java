package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullScreenPresenter;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullscreenAdapter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotFullscreenModule {

    @Provides
    ShotFullscreenAdapter provideShotsAdapter() {
        return new ShotFullscreenAdapter();
    }

    @Provides
    ShotFullScreenPresenter provideShotDetailsPresenter(ShotsController shotsController,
                                                        LikeShotController likeShotController,
                                                        BucketsController bucketsController,
                                                        UserShotsController userShotsController) {
        return new ShotFullScreenPresenter(shotsController, likeShotController, bucketsController,
                userShotsController);
    }
}