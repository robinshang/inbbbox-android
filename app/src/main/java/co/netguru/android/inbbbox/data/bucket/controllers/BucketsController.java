package co.netguru.android.inbbbox.data.bucket.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Single;

public interface BucketsController {

    Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount);

    Completable addShotToBucket(long bucketId, Shot shot);

    Single<List<BucketWithShots>> getCurrentUserBucketsWithShots(int pageNumber, int pageCount,
                                                                 int shotsCount, boolean shouldCache);

    Single<List<BucketWithShots>> getUserBucketsWithShots(long userId, int pageNumber, int pageCount,
                                                                 int shotsCount, boolean shouldCache);


    Single<List<Shot>> getShotsFromBucket(long bucketId, int pageNumber,
                                              int pageCount, boolean shouldCache);

    Single<Bucket> createBucket(@NonNull String name, @Nullable String description);

    Completable deleteBucket(long bucketId);

    Single<Boolean> isShotBucketed(long shotId);

    Single<List<Bucket>> getListBucketsForShot(long shotId);

    Completable removeShotFromBucket(long bucketId, Shot shot);
}
