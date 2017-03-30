package co.netguru.android.inbbbox.data.bucket.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.GuestModeBucketsRepository;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.cache.CacheValidator;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Single;

public class BucketsControllerGuest extends BaseBucketsController implements BucketsController {

    private final GuestModeBucketsRepository guestModeBucketsRepository;

    public BucketsControllerGuest(GuestModeBucketsRepository guestModeBucketsRepository,
                                UserApi userApi, BucketApi bucketApi,
                                UserController userController, CacheValidator cacheValidator) {
        super(userApi, bucketApi,userController, cacheValidator);
        this.guestModeBucketsRepository = guestModeBucketsRepository;
    }

    @Override
    public Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount) {
        return guestModeBucketsRepository.getUserBuckets();
    }

    @Override
    public Completable addShotToBucket(long bucketId, Shot shot) {
        return guestModeBucketsRepository.addShotToBucket(bucketId, shot);
    }

    @Override
    public Single<List<BucketWithShots>> getCurrentUserBucketsWithShots(int pageNumber, int pageCount,
                                                                        int shotsCount, boolean shouldCache) {
        return guestModeBucketsRepository.getUserBucketsWithShots();
    }

    @Override
    public Single<List<Shot>> getShotsListFromBucket(long bucketId, int pageNumber,
                                                     int pageCount, boolean shouldCache) {
        return guestModeBucketsRepository.getShotsListFromBucket(bucketId);
    }

    @Override
    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return guestModeBucketsRepository.createBucket(name, description);
    }

    @Override
    public Completable deleteBucket(long bucketId) {
        return guestModeBucketsRepository.removeBucket(bucketId);
    }

    @Override
    public Single<Boolean> isShotBucketed(long shotId) {
        return guestModeBucketsRepository.isShotBucketed(shotId);
    }

    @Override
    public Single<List<Bucket>> getListBucketsForShot(long shotId) {
        return guestModeBucketsRepository.getBucketsListForShot(shotId);
    }

    @Override
    public Completable removeShotFromBucket(long bucketId, Shot shot) {
        return guestModeBucketsRepository.removeShotFromBucket(bucketId, shot);
    }
}
