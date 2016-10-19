package co.netguru.android.inbbbox.db;

import java.io.Serializable;

import rx.Observable;

public interface CacheEndpoint {
    Observable save(String key, Serializable object);

    Observable get(String key, Class tClass);
}
