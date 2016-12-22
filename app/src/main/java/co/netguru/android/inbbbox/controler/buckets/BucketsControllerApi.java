package co.netguru.android.inbbbox.controler.buckets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.api.BucketApi;
import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class BucketsControllerApi implements BucketsController {

    private static final int FIRST_PAGE_NUMBER = 1;
    private final UserApi userApi;
    private final BucketApi bucketApi;

    public BucketsControllerApi(UserApi userApi, BucketApi bucketApi) {
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
                .flatMap(bucket -> getShotsListObservableFromBucket(bucket.id(), FIRST_PAGE_NUMBER, shotsCount),
                        BucketWithShots::create)
                .toList()
                .toSingle();
    }

    public Single<List<Shot>> getShotsListFromBucket(long bucketId, int pageNumber, int pageCount) {
        return getShotsListObservableFromBucket(bucketId, pageNumber, pageCount).toSingle();
    }

    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return bucketApi.createBucket(name, description);
    }

    public Completable deleteBucket(long bucketId) {
        return bucketApi.deleteBucket(bucketId);
    }

    private Observable<List<Shot>> getShotsListObservableFromBucket(long bucketId, int pageNumber, int pageCount) {
        return bucketApi.getBucketShotsList(bucketId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(Shot::create)
                .toList();
    }
}
