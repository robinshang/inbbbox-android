package co.netguru.android.inbbbox.db;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class CacheEndPointModule {

    @Singleton
    @Provides
    RealmStorage providesRealmStorage(RealmConfiguration configuration) {
        return new RealmStorage(configuration);
    }

    @Singleton
    @Provides
    CacheEndpoint provideCacheEndpoint(RealmStorage storage) {
        return new CacheEndPointImpl(storage);
    }
}
