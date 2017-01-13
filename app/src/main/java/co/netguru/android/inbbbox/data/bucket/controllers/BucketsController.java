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

    Single<List<BucketWithShots>> getUserBucketsWithShots(int pageNumber,
                                                          int pageCount, int shotsCount);


    Single<List<Shot>> getShotsListFromBucket(long bucketId, int pageNumber, int pageCount);

    Single<Bucket> createBucket(@NonNull String name, @Nullable String description);

    Completable deleteBucket(long bucketId);

    Single<Boolean> isShotBucketed(long shotId, long userId);

    Single<List<Bucket>> getListBucketsForShot(long shotId);

    Completable removeShotFromBucket(long bucketId, Shot shot);
}
