package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.db.CacheEndPointImpl;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.db.Storage;
import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class CacheEndpointModule {

    @Provides
    CacheEndpoint provideCacheEndpiont(Storage storage){
        return new CacheEndPointImpl(storage);
    }
}
