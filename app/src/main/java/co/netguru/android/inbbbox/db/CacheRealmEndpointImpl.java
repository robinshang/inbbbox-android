package co.netguru.android.inbbbox.db;

import android.util.Log;

import javax.inject.Inject;

import io.realm.RealmObject;
import rx.Observable;

public class CacheRealmEndpointImpl implements CacheEndpoint {

    private static final String LOG_TAG = CacheRealmEndpointImpl.class.getSimpleName();
    private RealmStorage realmStorage;

    @Inject
    public CacheRealmEndpointImpl(RealmStorage realmStorage) {

        this.realmStorage = realmStorage;
    }

    @Override
    public Observable save(Object object) {
        Log.d(LOG_TAG, "Type of the object to save: " + object.getClass());
        return realmStorage.save(object.getClass(), (RealmObject) object)
                .doOnSubscribe(this::openDb)
                .doOnCompleted(this::closeDb);
    }

    private void closeDb() {
        Log.d(LOG_TAG, "Closing Db");
        realmStorage.closeDb();
    }

    @Override
    public Observable get(Class aClass) {
        return realmStorage.get(aClass)
                .doOnSubscribe(() -> realmStorage.openDb())
                .doOnCompleted(() -> realmStorage.closeDb());
    }

    private void openDb() {
        Log.d(LOG_TAG, "Opening Db");
        realmStorage.openDb();
    }
}
