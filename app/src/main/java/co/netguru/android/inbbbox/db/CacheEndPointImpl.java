package co.netguru.android.inbbbox.db;

import android.util.Log;

import java.io.Serializable;

import javax.inject.Inject;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class CacheEndPointImpl implements CacheEndpoint {

    private Storage storage;

    @Inject
    public CacheEndPointImpl(Storage storage) {

        this.storage = storage;
    }

    public Observable save(String key, Serializable object) {
        return Observable.create(subscriber -> {
            try {
                storage.put(key, object);
                subscriber.onNext(object);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }

        });
    }

    public Observable get(String key, Class tokenClass) {
        return Observable.create(subscriber -> {
            Serializable result = null;
            try {
                storage.get(key, tokenClass);
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
