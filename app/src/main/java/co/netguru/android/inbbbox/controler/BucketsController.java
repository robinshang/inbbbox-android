package co.netguru.android.inbbbox.controler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.BucketApi;
import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class BucketsController {

    private static final int FIRST_PAGE_NUMBER = 1;
    private final UserApi userApi;
    private final BucketApi bucketApi;

    @Inject
    BucketsController(UserApi userApi, BucketApi bucketApi) {
        this.userApi = userApi;
        this.bucketApi = bucketApi;
    }

    public Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount);
    }

    public Completable addShotToBucket(long bucketId, long shotId) {
        return bucketApi.addShotToBucket(bucketId, shotId);
    }

    public Single<List<BucketWithShots>> getUserBucketsWithShots(int pageNumber, int pageCount, int shotsCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> bucketApi.getBucketShotsList(bucket.id(), FIRST_PAGE_NUMBER, shotsCount).toObservable(),
                        BucketWithShots::create)
                .toList()
                .toSingle();
    }

    public Single<List<ShotEntity>> getShotsListFromBucket(long bucketId, int pageNumber, int pageCount) {
        return bucketApi.getBucketShotsList(bucketId, pageNumber, pageCount);
    }

    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return bucketApi.createBucket(name, description);
    }

    public Completable deleteBucket(long bucketId) {
        return bucketApi.deleteBucket(bucketId);
    }
}
