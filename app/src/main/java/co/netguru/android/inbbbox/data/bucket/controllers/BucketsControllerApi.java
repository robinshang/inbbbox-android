package co.netguru.android.inbbbox.data.bucket.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
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
                .flatMap(bucket -> getShotsListObservableFromBucket(bucket.id(), FIRST_PAGE_NUMBER, shotsCount),
                        BucketWithShots::create)
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
    public Single<Boolean> isShotBucketed(long shotId, long userId) {
        return bucketApi.getShotBucketsList(shotId)
                .flatMapObservable(Observable::from)
                .map(bucket -> bucket.user() != null ? bucket.user().id() : Constants.UNDEFINED)
                .contains(userId)
                .toSingle();
    }

    private Observable<List<Shot>> getShotsListObservableFromBucket(long bucketId, int pageNumber, int pageCount) {
        return bucketApi.getBucketShotsList(bucketId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(Shot::create)
                .toList();
    }
}