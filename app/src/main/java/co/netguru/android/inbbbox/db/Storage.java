package co.netguru.android.inbbbox.db;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import rx.Observable;

public interface Storage {
    Realm openDb();

    void closeDb();

    <RealmEntity extends RealmObject> Observable<RealmEntity> get(Class<RealmEntity> vClass);

    <RealmEntity extends RealmObject> void save(Realm.Transaction transaction);
}
