package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shots.ShotsMapper;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;
import dagger.Module;
import dagger.Provides;

@FragmentScope
@Module
public class ShotsModule {

    @Provides
    ShotsMapper provideShotsMapper() {
        return new ShotsMapper();
    }

    @Provides
    ShotsAdapter provideShotsAdapter(ImageLoader loader) {
        return new ShotsAdapter(loader);
    }
}
