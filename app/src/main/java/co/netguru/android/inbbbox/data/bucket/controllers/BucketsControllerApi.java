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

    public Observable<BucketWithShots> getFromCacheOrCreate(Bucket bucket1, int pageNumber, int shotsCount) {
        BucketWithShots bucketWithShots = bucketWithShotsCache.get(bucket1.id());
        if (bucketWithShots != null) {
            return Observable.just(bucketWithShots);
        } else {
            return Observable.just(bucket1)
                    .flatMap(bucket -> getShotsListObservableFromBucket(bucket.id(), pageNumber, shotsCount),
                            BucketWithShots::create)
                    .flatMap(bucketWithShots1 -> {
                        bucketWithShotsCache.add(bucketWithShots1);
                        return Observable.just(bucketWithShots1);
                    });
        }
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

        BucketWithShots bucketWithShots = bucketCache.get(bucketId);

        if (bucketWithShots != null) {
            return Observable.just(bucketWithShots.shots());
        } else {
            return bucketApi.getBucketShotsList(bucketId, pageNumber, pageCount)
                    .flatMapObservable(Observable::from)
                    .map(Shot::create)
                    .toList()
                    .map(list -> {
                        Bucket bucket = Bucket.builder()
                                .id(bucketId)
                                .name("")
                                .createdAt(ZonedDateTime.now())
                                .shotsCount(list.size())
                                .build();
                        bucketCache.add(BucketWithShots.create(bucket, list));
                        return list;
                    });
        }
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
