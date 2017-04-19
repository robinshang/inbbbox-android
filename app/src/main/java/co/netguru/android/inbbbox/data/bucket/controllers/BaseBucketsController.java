package co.netguru.android.inbbbox.data.bucket.controllers;

import java.util.List;

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

abstract class BaseBucketsController {

    static final int FIRST_PAGE_NUMBER = 1;
    protected final UserController userController;
    final UserApi userApi;
    final BucketApi bucketApi;
    final CacheValidator cacheValidator;

    BaseBucketsController(UserApi userApi, BucketApi bucketApi,
                          UserController userController, CacheValidator cacheValidator) {
        this.userApi = userApi;
        this.cacheValidator = cacheValidator;
        this.bucketApi = bucketApi;
        this.userController = userController;
    }

    public Single<List<BucketWithShots>> getUserBucketsWithShots(long userId, int pageNumber,
                                                                 int pageCount, int shotsCount,
                                                                 boolean shouldCache) {
        return userApi.getUserBucketsList(userId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> getShotsFromBucketObservable(bucket.id(), FIRST_PAGE_NUMBER,
                        shotsCount, shouldCache), (Bucket bucket, List<Shot> shots) ->
                        BucketWithShots.create(bucket, shots, shots.size() >= shotsCount))
                .toList()
                .toSingle();
    }

    Observable<List<Shot>> getShotsFromBucketObservable(long bucketId, int pageNumber,
                                                        int pageCount, boolean shouldCache) {
        return cacheValidator.isCacheValid(CacheValidator.CACHE_BUCKET_SHOTS)
                .flatMap(isCacheValid -> bucketApi.getBucketShotsList(bucketId,
                        pageNumber, pageCount,
                        shouldCache && isCacheValid ?
                                CacheStrategy.mediumCache() : CacheStrategy.noCache()))
                .flatMapObservable(Observable::from)
                .map(Shot::create)
                .toList();
    }

}
