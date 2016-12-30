package co.netguru.android.inbbbox.feature.shot.detail;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotsDetailsModule {

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
                                                     ErrorController errorController) {
        return new ShotDetailsPresenter(shotDetailsController, errorController);
    }
}
