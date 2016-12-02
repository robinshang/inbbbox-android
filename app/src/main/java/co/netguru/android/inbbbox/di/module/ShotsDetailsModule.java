package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.controler.UserController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsPresenter;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotsDetailsModule {
    private final DetailsViewActionCallback callback;

    public ShotsDetailsModule(DetailsViewActionCallback callback) {
        this.callback = callback;
    }

    @Provides
    ShotDetailsAdapter provideAdapter() {
        return new ShotDetailsAdapter(callback);
    }

    @Provides
    ShotDetailsController provideShotDetailsController(LikeShotController likeShotController,
                                                       ShotsApi shotsApi,
                                                       UserController userController) {
        return new ShotDetailsController(likeShotController, shotsApi, userController);
    }

    @Provides
    ShotDetailsPresenter provideShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                                     ErrorMessageController messageController,
                                                     UserShotsController userShotsController) {
        return new ShotDetailsPresenter(shotDetailsController, messageController, userShotsController);
    }
}

