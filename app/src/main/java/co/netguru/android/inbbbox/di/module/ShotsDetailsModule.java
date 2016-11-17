package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
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
    ShotDetailsAdapter provideAdapter(LocalTimeFormatter formatter) {
        return new ShotDetailsAdapter(formatter, callback);
    }

    @Provides
    ShotDetailsController providerShotDetailsController(){
        return new ShotDetailsController();
    }
}
