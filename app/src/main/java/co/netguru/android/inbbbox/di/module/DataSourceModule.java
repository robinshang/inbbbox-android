package co.netguru.android.inbbbox.di.module;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.db.datasource.SettingsDataSourceImpl;
import co.netguru.android.inbbbox.db.datasource.TokenDataSourceImpl;
import co.netguru.android.inbbbox.db.datasource.UserDataSourceImpl;
import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class DataSourceModule {

    @Provides
    DataSource<Token> provideTokenDataSource(Storage storage) {
        return new TokenDataSourceImpl(storage);
    }

    @Provides
    DataSource<User> provideUserDataSource(Storage storage) {
        return new UserDataSourceImpl(storage);
    }

    @Provides
    DataSource<Settings> provideSettingsDataSource(Storage storage) {
        return new SettingsDataSourceImpl(storage);
    }
}
