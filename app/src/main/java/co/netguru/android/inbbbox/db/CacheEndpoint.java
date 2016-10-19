package co.netguru.android.inbbbox.db;

import java.io.Serializable;

import rx.Observable;

public interface CacheEndpoint<T> {
    Observable<T> save(String key, Serializable object);

    Observable<T> get(String key, Class tClass);
}
