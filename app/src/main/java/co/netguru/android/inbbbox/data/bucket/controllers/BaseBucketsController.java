package co.netguru.android.inbbbox.data.bucket.controllers;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import co.netguru.android.inbbbox.data.Cache;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.cache.CacheValidator;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

abstract class BaseBucketsController {

    static final int FIRST_PAGE_NUMBER = 1;
    protected final UserController userController;
    final UserApi userApi;
    final BucketApi bucketApi;
    final CacheValidator cacheValidator;
    final Cache<BucketWithShots> bucketWithShotsCache;
    final Cache<BucketWithShots> bucketCache;

    BaseBucketsController(UserApi userApi, BucketApi bucketApi,
                          UserController userController, CacheValidator cacheValidator) {
        this.userApi = userApi;
        this.cacheValidator = cacheValidator;
        this.bucketApi = bucketApi;
        this.userController = userController;
        this.bucketWithShotsCache = new Cache<>();
        this.bucketCache = new Cache<>();
    }

    public Single<List<BucketWithShots>> getUserBucketsWithShots(long userId, int pageNumber,
                                                                 int pageCount, int shotsCount,
                                                                 boolean shouldCache) {
        return userApi.getUserBucketsList(userId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> getShotsListObservableFromBucket(bucket.id(), 1,
                        shotsCount, shouldCache), BucketWithShots::create)
                .toList()
                .toSingle();
    }

    Observable<List<Shot>> getShotsListObservableFromBucket(long bucketId, int pageNumber,
                                                            int pageCount, boolean shouldCache) {
        return Observable.just(bucketCache.get(bucketId))
                .filter(bucketWithShots -> bucketWithShots != null)
                .map(BucketWithShots::shots)
                .switchIfEmpty(downloadAndCacheShotsList(bucketId, pageNumber, pageCount, shouldCache));
    }

    Observable<List<Shot>> downloadAndCacheShotsList(long bucketId, int pageNumber,
                                                     int pageCount, boolean shouldCache) {
        return cacheValidator.isCacheValid(CacheValidator.CACHE_BUCKET_SHOTS)
                .flatMap(isCacheValid -> bucketApi.getBucketShotsList(bucketId,
                        pageNumber, pageCount,
                        shouldCache && isCacheValid ?
                                CacheStrategy.mediumCache() : CacheStrategy.noCache()))
                .flatMapObservable(Observable::from)
                .map(Shot::create)
                .toList()
                .map(list -> addShotsToCache(bucketId, list));
    }

    private List<Shot> addShotsToCache(long bucketId, List<Shot> shots) {
        Bucket bucket = Bucket.builder()
                .id(bucketId)
                .name("")
                .createdAt(ZonedDateTime.now())
                .shotsCount(shots.size())
                .build();
        bucketCache.add(BucketWithShots.create(bucket, shots));
        return shots;
    }
}
