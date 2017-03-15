package co.netguru.android.inbbbox.data.cache;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Single;

@Singleton
public class CacheValidator {

    public static final String CACHE_BUCKET_SHOTS = "bucket_shots";

    private SharedPreferences sharedPreferences;

    @Inject
    public CacheValidator(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Completable validateCache(String key) {
        return setKey(key, true);
    }

    public Completable invalidateCache(String key) {
        return setKey(key, false);
    }

    public Single<Boolean> isCacheValid(String key) {
        return Single.just(isCacheValidInternal(key));
    }

    private Completable setKey(String key, boolean valid) {
        return Completable.fromAction(() -> setKeyInternal(key, valid));
    }

    private void setKeyInternal(String key, boolean valid) {
        sharedPreferences.edit()
                .putBoolean(key, valid)
                .apply();
    }

    private boolean isCacheValidInternal(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}
