package co.netguru.android.inbbbox.data.bucket.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.data.Cache;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class BucketsControllerApi implements BucketsController {

    private static final int FIRST_PAGE_NUMBER = 1;
    private final UserApi userApi;
    private final BucketApi bucketApi;
    private final UserController userController;
    private final Cache<BucketWithShots> bucketWithShotsCache;
    private final Cache<BucketWithShots> bucketCache;

    public BucketsControllerApi(UserApi userApi, BucketApi bucketApi,
                                UserController userController) {
        this.userApi = userApi;
        this.bucketApi = bucketApi;
        this.userController = userController;
        this.bucketWithShotsCache = new Cache<>();
        this.bucketCache = new Cache<>();
    }

    @Override
    public Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount);
    }

    @Override
    public Completable addShotToBucket(long bucketId, Shot shot) {
        return bucketApi.addShotToBucket(bucketId, shot.id());
    }

    @Override
    public Single<List<BucketWithShots>> getUserBucketsWithShots(int pageNumber, int pageCount, int shotsCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> getFromCacheOrCreate(bucket, FIRST_PAGE_NUMBER, shotsCount))
                .toList()
                .toSingle();
    }

    @Override
    public Single<List<Shot>> getShotsListFromBucket(long bucketId, int pageNumber, int pageCount) {
        return getShotsListObservableFromBucket(bucketId, pageNumber, pageCount).toSingle();
    }

    @Override
    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return bucketApi.createBucket(name, description);
    }

    @Override
    public Completable deleteBucket(long bucketId) {
        return bucketApi.deleteBucket(bucketId);
    }

    @Override
    public Single<Boolean> isShotBucketed(long shotId) {
        return getCurrentUserId()
                .flatMap(currentUserId -> checkIfShotIsInUserBuckets(shotId, currentUserId));
    }

    @Override
    public Single<List<Bucket>> getListBucketsForShot(long shotId) {
        return getCurrentUserId()
                .flatMap(currentUserId -> getUserBucketsListForShot(currentUserId, shotId));
    }

    @Override
    public Completable removeShotFromBucket(long bucketId, Shot shot) {
        return bucketApi.removeShotFromBucket(bucketId, shot.id());
    }

    private Observable<List<Shot>> getShotsListObservableFromBucket(long bucketId, int pageNumber, int pageCount) {
        return Observable.just(bucketCache.get(bucketId).shots())
                .filter(val -> val != null)
                .switchIfEmpty(downloadAndCacheShotsList(bucketId, pageNumber, pageCount));
    }

    private Observable<List<Shot>> downloadAndCacheShotsList(long bucketId, int pageNumber, int pageCount) {
        return bucketApi.getBucketShotsList(bucketId, pageNumber, pageCount)
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

    private Single<List<Bucket>> getUserBucketsListForShot(long userId, long shotId) {
        return bucketApi.getShotBucketsList(shotId)
                .flatMapObservable(Observable::from)
                .filter(bucket -> bucket.user() != null && bucket.user().id() == userId)
                .toList()
                .toSingle();
    }

    private Single<Long> getCurrentUserId() {
        return userController.getUserFromCache()
                .map(User::id)
                .onErrorResumeNext(throwable -> Single.just((long) Constants.UNDEFINED));
    }

    private Single<Boolean> checkIfShotIsInUserBuckets(long shotId, long userId) {
        return bucketApi.getShotBucketsList(shotId)
                .flatMapObservable(Observable::from)
                .map(bucket -> bucket.user() != null ? bucket.user().id() : Constants.UNDEFINED)
                .contains(userId)
                .toSingle();
    }

    private Observable<BucketWithShots> fetchAndCacheBucket(Bucket parameterBucket, int pageNumber, int shotsCount) {
        return Observable.just(parameterBucket)
                .flatMap(bucket -> getShotsListObservableFromBucket(bucket.id(), pageNumber, shotsCount),
                        BucketWithShots::create)
                .flatMap(bucketWithShots -> {
                    bucketWithShotsCache.add(bucketWithShots);
                    return Observable.just(bucketWithShots);
                });
    }

    private Observable<BucketWithShots> getFromCacheOrCreate(Bucket parameterBucket, int pageNumber, int shotsCount) {
        return Observable.just(bucketWithShotsCache.get(parameterBucket.id()))
                .filter(cachedBucketWithShots -> cachedBucketWithShots != null)
                .switchIfEmpty(fetchAndCacheBucket(parameterBucket, pageNumber, shotsCount));
    }
}
