package co.netguru.android.inbbbox.feature.shot.detail;

import java.util.List;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotsDetailsModule {

    private final List<Shot> allShots;

    public ShotsDetailsModule(List<Shot> allShots) {
        this.allShots = allShots;
    }

    @Provides
    @FragmentScope
    ShotDetailsController provideShotDetailsController(LikeShotController likeShotController,
                                                       BucketsController bucketsController,
                                                       ShotsApi shotsApi, UserController userController) {
        return new ShotDetailsController(likeShotController, bucketsController, shotsApi, userController);
    }

    @Provides
    @FragmentScope
    ShotDetailsPresenter provideShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                                     ErrorController errorController,
                                                     RxBus rxBus,
                                                     BucketsController bucketsController) {
        return new ShotDetailsPresenter(shotDetailsController, errorController, allShots,
                rxBus, bucketsController);

    }
}
