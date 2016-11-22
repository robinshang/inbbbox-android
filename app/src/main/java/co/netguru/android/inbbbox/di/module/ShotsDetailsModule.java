package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
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
    ShotDetailsController provideShotDetailsController(ShotCommentsApi shotCommentsApi,
                                                       LikeShotController likeShotController) {
        return new ShotDetailsController(shotCommentsApi, likeShotController);
    }

    @Provides
    ShotDetailsPresenter provideShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                                     ErrorMessageController messageController) {
        return new ShotDetailsPresenter(shotDetailsController, messageController);
    }
}

