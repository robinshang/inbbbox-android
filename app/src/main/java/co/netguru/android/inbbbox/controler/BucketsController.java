package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.BucketApi;
import co.netguru.android.inbbbox.api.UserApi;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.BucketShot;
import rx.Completable;
import rx.Single;

@Singleton
public class BucketsController {

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

    public Single<List<BucketShot>> getBucketShots(long bucketId) {
        return bucketApi.getBucketShots(bucketId);
    }

    public Completable addShotToBucket(long bucketId, long shotId) {
        return bucketApi.addShotToBucket(bucketId, shotId);
    }
}
