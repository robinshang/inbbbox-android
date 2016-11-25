package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.BucketApi;
import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class BucketsController {

    public static final int FIRST_PAGE_NUMBER = 1;
    private final UserApi userApi;
    private final BucketApi bucketApi;

    @Inject
    BucketsController(UserApi userApi, BucketApi bucketApi) {
        this.userApi = userApi;
        this.bucketApi = bucketApi;
    }

    public Single<List<Bucket>> getCurrentUserBuckets() {
        return userApi.getUserBucketsList();
    }

    public Single<List<Bucket>> getCurrentUserBuckets(int pageNumber, int pageCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount);
    }

    public Completable addShotToBucket(long bucketId, long shotId) {
        return bucketApi.addShotToBucket(bucketId, shotId);
    }

    public Single<List<BucketWithShots>> getBucketWithShots(int pageNumber, int pageCount, int shotsCount) {
        return userApi.getUserBucketsList(pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .flatMap(bucket -> bucketApi.getBucketShots(bucket.id(), FIRST_PAGE_NUMBER, shotsCount).toObservable(),
                        BucketWithShots::create)
                .toList()
                .toSingle();
    }
}
