package co.netguru.android.inbbbox.db;

import co.netguru.android.commons.di.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module
public class CacheEndpointModule {

    @Provides
    @ActivityScope
    CacheEndpoint provideCacheEndpiont(Storage storage){
        return new CacheEndPointImpl(storage);
    }
}
