package co.netguru.android.inbbbox.db;

import io.realm.RealmModel;
import io.realm.RealmObject;
import rx.Observable;

public interface Storage {
    void openDb();

    void closeDb();

    <RealmEntity extends RealmObject> Observable<RealmEntity> get(Class<RealmEntity> vClass);

    <RealmEntity extends RealmObject> Observable<Boolean> save(Class<RealmEntity> aClass,
                                                               RealmObject toSave);
}
