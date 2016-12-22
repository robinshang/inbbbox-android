package co.netguru.android.inbbbox.feature.shot.detail;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.controller.ErrorController;
import co.netguru.android.inbbbox.controller.likescontroller.LikeShotController;
import co.netguru.android.inbbbox.controller.ShotDetailsController;
import co.netguru.android.inbbbox.controller.UserController;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class ShotsDetailsModule {

    @Provides
    @FragmentScope
    ShotDetailsController provideShotDetailsController(LikeShotController likeShotController,
                                                       ShotsApi shotsApi,
                                                       UserController userController) {
        return new ShotDetailsController(likeShotController, shotsApi, userController);
    }

    @Provides
    @FragmentScope
    ShotDetailsPresenter provideShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                                     ErrorController errorController) {
        return new ShotDetailsPresenter(shotDetailsController, errorController);
    }
}

