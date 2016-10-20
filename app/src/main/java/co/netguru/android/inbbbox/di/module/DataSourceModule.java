package co.netguru.android.inbbbox.di.module;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.db.datasource.TokenDataSourceImpl;
import co.netguru.android.inbbbox.db.datasource.UserDataSourceImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    DataSource<Token> provideTokenDataSource(Storage storage) {
        return new TokenDataSourceImpl(storage);
    }

    @Provides
    @Singleton
    DataSource<User> provideUserDataSource(Storage storage) {
        return new UserDataSourceImpl(storage);
    }
}
