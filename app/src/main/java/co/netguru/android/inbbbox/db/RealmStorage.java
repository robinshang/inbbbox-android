package co.netguru.android.inbbbox.db;

import co.netguru.android.inbbbox.utils.Constants;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.Realm;
import rx.Observable;

public class RealmStorage implements Storage {

    private Realm realm = null;
    RealmConfiguration configuration;

    public RealmStorage() {
    }

    public Realm openDb() {
        configuration = new RealmConfiguration
                .Builder()
                .name(Constants.Realm.REALM_FILEM_NAME)
                .build();

        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();
        return realm;
    }

    public void closeDb() {
        realm.close();
    }

    public <RealmEntity extends RealmObject> Observable<RealmEntity> get(Class<RealmEntity> vClass) {
        return Observable.defer(() -> realm.where(vClass).findAll().asObservable())
                .map(vs -> vs.first());
    }

    public <RealmEntity extends RealmObject> void save(Realm.Transaction transaction) {
        openDb();
        realm.executeTransaction(transaction);
        closeDb();

    }
}
