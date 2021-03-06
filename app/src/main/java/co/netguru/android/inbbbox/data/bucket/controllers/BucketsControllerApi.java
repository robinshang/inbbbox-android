package co.netguru.android.inbbbox.data.bucket.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.cache.CacheValidator;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class BucketsControllerApi extends BaseBucketsController implements BucketsController {

    public BucketsControllerApi(UserApi userApi, BucketApi bucketApi,
                                UserController userController, CacheValidator cacheValidator) {
        super(userApi, bucketApi, userController, cacheValidator);
    }

    @Override
    public Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount) {
        return userApi.getAuthenticatedUserBucketsList(pageNumber, pageCount);
    }

    @Override
    public Completable addShotToBucket(long bucketId, Shot shot) {
        return bucketApi.addShotToBucket(bucketId, shot.id());
    }

    @Override
    public Single<List<BucketWithShots>> getCurrentUserBucketsWithShots
            (int pageNumber, int pageCount, int shotsCount, boolean shouldCache) {
        return userApi.getAuthenticatedUserBucketsList(pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> getShotsFromBucketObservable(bucket.id(), FIRST_PAGE_NUMBER,
                        shotsCount, shouldCache), BucketWithShots::create)
                .toList()
                .doOnNext(bucketWithShotses ->
                        cacheValidator.validateCache(CacheValidator.CACHE_BUCKET_SHOTS).subscribe())
                .toSingle();
    }

    @Override
    public Single<List<Shot>> getShotsFromBucket(long bucketId, int pageNumber, int pageCount, boolean shouldCache) {
        return getShotsFromBucketObservable(bucketId, pageNumber, pageCount, shouldCache).toSingle();
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

}
