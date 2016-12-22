package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.likescontroller.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.controler.UserController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsPresenter;
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

