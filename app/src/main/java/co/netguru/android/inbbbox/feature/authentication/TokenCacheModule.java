package co.netguru.android.inbbbox.feature.authentication;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.db.RealmObjectMapper;
import co.netguru.android.inbbbox.db.Storage;
import dagger.Module;
import dagger.Provides;

@Module
public class TokenCacheModule {

    @Provides
    @ActivityScope
    CacheEndpoint provideTokeCacheEndpoint(Storage storage, RealmObjectMapper realmObjectMapper) {
        return new TokenCacheEndpoint(storage, realmObjectMapper);
    }
}
