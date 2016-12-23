package co.netguru.android.inbbbox.controler.buckets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.localrepository.database.GuestModeBucketsRepository;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Single;

public class BucketsControllerGuest implements BucketsController {

    private final GuestModeBucketsRepository guestModeBucketsRepository;

    public BucketsControllerGuest(GuestModeBucketsRepository guestModeBucketsRepository) {
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
    public Single<List<BucketWithShots>> getUserBucketsWithShots(int pageNumber, int pageCount, int shotsCount) {
        return guestModeBucketsRepository.getUserBucketsWithShots();
    }

    @Override
    public Single<List<Shot>> getShotsListFromBucket(long bucketId, int pageNumber, int pageCount) {

        return Single.just(new ArrayList<>());
    }

    @Override
    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return guestModeBucketsRepository.createBucket(name, description);
    }

    @Override
    public Completable deleteBucket(long bucketId) {
        return Completable.complete();
    }
}
