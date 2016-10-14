package co.netguru.android.inbbbox.db;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func0;

public class RealmStorage implements Storage {

    private Realm realm;

    public RealmStorage(RealmConfiguration configuration) {
        Realm.setDefaultConfiguration(configuration);
    }

    public void openDb() {
        realm = Realm.getDefaultInstance();
    }

    public void closeDb() {
        realm.close();
    }

    public <RealmEntity extends RealmObject> Observable<RealmEntity> get(Class<RealmEntity> vClass) {
        return Observable.defer(() -> realm.where(vClass).findAll().asObservable())
                .map(vs -> vs.first());
    }

    public <RealmEntity extends RealmObject> Observable<Boolean> save(Class<RealmEntity> aClass,
                                                                      RealmObject toSave) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                saveToDb(aClass, toSave);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    private void saveToDb(Class aClass, RealmObject toSave) {
        realm.beginTransaction();
        realm.copyToRealm(toSave);
        realm.commitTransaction();

    }

}
