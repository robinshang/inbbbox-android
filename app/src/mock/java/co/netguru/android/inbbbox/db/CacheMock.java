package co.netguru.android.inbbbox.db;

import rx.Observable;

public class CacheMock implements CacheEndpoint {
    @Override
    public Observable save(Object object) {
        return null;
    }

    @Override
    public Observable get(Class aClass) {
        return null;
    }
}
