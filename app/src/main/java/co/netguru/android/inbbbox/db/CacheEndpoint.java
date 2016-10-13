package co.netguru.android.inbbbox.db;

import io.realm.RealmModel;
import rx.Observable;

public interface CacheEndpoint<T> {
    Observable save(T object);

    Observable<T> get(Class<T> tClass);


}
