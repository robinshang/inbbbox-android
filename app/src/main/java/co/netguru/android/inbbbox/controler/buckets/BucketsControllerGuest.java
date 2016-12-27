package co.netguru.android.inbbbox.controler.buckets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.netguru.android.inbbbox.localrepository.database.GuestModeBucketsRepository;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

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
    public Completable isShotBucketed(long shotId, long userId) {
        return guestModeBucketsRepository.isShotBucketed(shotId)
                .doOnCompleted(() -> Timber.d("Shot is bucketed"))
                .doOnError(throwable -> Timber.d("Shot is not bucketed"));
    }
}
