package co.netguru.android.inbbbox.feature.authentication;

import android.util.Log;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.data.realm.RealmToken;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.inbbbox.db.RealmObjectMapper;
import co.netguru.android.inbbbox.db.RealmStorage;
import co.netguru.android.inbbbox.db.Storage;
import io.realm.Realm;
import io.realm.RealmModel;
import rx.Observable;
import rx.Subscriber;

public class TokenCacheEndpoint implements CacheEndpoint<Token> {

    private static final String LOG_TAG = TokenCacheEndpoint.class.getSimpleName();
    private Storage realmStorage;
    private RealmObjectMapper realmObjectMapper;

    @Inject
    public TokenCacheEndpoint(Storage storage, RealmObjectMapper realmObjectMapper) {

        this.realmStorage = storage;
        this.realmObjectMapper = realmObjectMapper;
    }

    public Observable save(Token object) {
        Log.d(LOG_TAG, "Type of the object to save: " + object.getClass());
        return Observable.create(subscriber -> {
            realmStorage.save(realm -> {
                RealmModel token = realmObjectMapper.toRealmObject(object, realm);
                realm.insertOrUpdate(token);
                subscriber.onNext(true);
                subscriber.onCompleted();

            });

        });
    }

    public Observable get(Class aClass) {
        return realmStorage.get(aClass)
                .doOnSubscribe(() -> realmStorage.openDb())
                .doOnCompleted(() -> realmStorage.closeDb());
    }

    private Realm openDb() {
        Log.d(LOG_TAG, "Opening Db");
        return realmStorage.openDb();
    }
}
