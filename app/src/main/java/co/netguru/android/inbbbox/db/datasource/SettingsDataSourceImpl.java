package co.netguru.android.inbbbox.db.datasource;

import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import timber.log.Timber;

public class SettingsDataSourceImpl implements DataSource<Settings> {

    private final Storage storage;

    public SettingsDataSourceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Observable<Boolean> save(Settings Settings) {
        return Observable.just(Settings)
                .map(this::saveSettingsToDatabase);
    }

    @Override
    public Observable<Settings> get() {
        return Observable.just(Constants.Db.SETTINGS_KEY)
                .flatMap(this::getSettingsFromDatabase);
    }

    private boolean saveSettingsToDatabase(Settings Settings) {
        boolean result;
        try {
            storage.put(Constants.Db.SETTINGS_KEY, Settings);

            result = true;
        } catch (Exception e) {
            //Use i() for loging exception from SnappyDb because that is how SnappyI inform that there is no such object in Db
            Timber.i(e, "Error while saving Settings to database");

            result = false;
        }
        return result;
    }

    private Observable<Settings> getSettingsFromDatabase(String key) {
        try {
            return Observable.just(storage.get(key, Settings.class));
        } catch (Exception e) {
            Timber.i(e, "Error while getting Settings from database");

            return Observable.error(e);
        }
    }
}
