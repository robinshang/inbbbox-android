package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.api.ShotDetailsApi;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsPresenter;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotsDetailsModule {
    private long shotId;
    private final DetailsViewActionCallback callback;

    public ShotsDetailsModule(long shotId, DetailsViewActionCallback callback) {
        this.shotId = shotId;

        this.callback = callback;
    }

    @Provides
    ShotDetailsAdapter provideAdapter(LocalTimeFormatter formatter) {
        return new ShotDetailsAdapter(formatter, callback);
    }

    @Provides
    ShotDetailsController provideShotDetailsController(ShotDetailsApi detailsApi,
                                                       ShotCommentsApi shotCommentsApi) {
        return new ShotDetailsController(detailsApi, shotCommentsApi);
    }

    @Provides
    ShotDetailsPresenter provideShotDetailsPresenter(ShotDetailsController shotDetailsController,
                                               ErrorMessageController messageController) {
        return new ShotDetailsPresenter(shotId, shotDetailsController, messageController);
    }
}
