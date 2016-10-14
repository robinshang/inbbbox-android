package co.netguru.android.inbbbox.db;

import io.realm.RealmObject;
import rx.Observable;

public class StorageMock implements Storage {
    @Override
    public void openDb() {

    }

    @Override
    public void closeDb() {

    }

    @Override
    public <RealmEntity extends RealmObject> Observable<RealmEntity> get(Class<RealmEntity> vClass) {
        return null;
    }

    @Override
    public <RealmEntity extends RealmObject> Observable<Boolean> save(Class<RealmEntity> aClass, RealmObject toSave) {
        return null;
    }
}
