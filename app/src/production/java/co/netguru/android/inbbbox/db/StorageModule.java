package co.netguru.android.inbbbox.db;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class StorageModule {

    @Singleton
    @Provides
    Storage providesRealmStorage(RealmConfiguration configuration) {
        return new RealmStorage(configuration);
    }
}
