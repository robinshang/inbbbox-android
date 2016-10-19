package co.netguru.android.inbbbox.di.module;

import android.content.Context;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.db.StorageMock;
import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Singleton
    @Provides
    Storage providesRealmStorage(Context context) {
        return new StorageMock();
    }
}
