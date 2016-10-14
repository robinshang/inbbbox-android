package co.netguru.android.inbbbox.db;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Singleton
    @Provides
    Storage providesRealmStorage(Context context) {
        return new SynchronizedDb(context);
    }
}
