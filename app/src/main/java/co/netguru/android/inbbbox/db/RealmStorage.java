package co.netguru.android.inbbbox.db;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func0;

public class RealmStorage {

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

    public <V extends RealmModel> Observable<V> get(Class<V> vClass) {
        return Observable.defer(() -> realm.where(vClass).findAll().asObservable())
                .map(vs -> vs.first());
    }

    public Observable<Boolean> save(Class aClass, RealmObject toSave) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                saveToDb(aClass, toSave);
                subscriber.onNext(true);
            }
        });
    }

    private void saveToDb(Class aClass, RealmObject toSave) {
        realm.beginTransaction();
        realm.copyToRealm(toSave);
        realm.commitTransaction();

    }
}
