package co.netguru.android.inbbbox.localrepository.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.BucketDbMapper;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class GuestModeBucketsRepository {

    private final DaoSession daoSession;

    @Inject
    public GuestModeBucketsRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Single<List<Bucket>> getUserBuckets() {
        Timber.d("Getting buckets from local repository");
        return daoSession.getBucketDBDao().queryBuilder().rx().oneByOne()
                .map(Bucket::fromDB).toList().toSingle();
    }

    public Completable addShotToBucket(long bucketId, Shot shot) {
        Timber.d("Adding shot to bucket from local repository");
        return Completable.complete();
    }

    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        return daoSession.getBucketDBDao().rx()
                .insertOrReplace(BucketDbMapper.createNewBucket(name, description))
                .map(Bucket::fromDB)
                .toSingle();
    }
}
