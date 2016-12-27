package co.netguru.android.inbbbox.localrepository.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.localrepository.database.BucketDB;
import co.netguru.android.inbbbox.model.localrepository.database.BucketDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.JoinBucketsWithShots;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.BucketDbMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class GuestModeBucketsRepository {

    private static final String SHOT_IS_NOT_BUCKETED_ERROR = "Shot is not bucketed!";

    private final DaoSession daoSession;

    @Inject
    GuestModeBucketsRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Single<List<Bucket>> getUserBuckets() {
        Timber.d("Getting buckets from local repository");
        return daoSession.getBucketDBDao().queryBuilder().rx().oneByOne()
                .map(Bucket::fromDB).toList().toSingle();
    }

    public Completable addShotToBucket(long bucketId, Shot shot) {
        Timber.d("Adding shot to bucket from local repository");
        return daoSession.getBucketDBDao().queryBuilder().rx().oneByOne()
                .filter(bucketDB -> bucketDB.getId() == bucketId)
                .flatMap(bucketDB -> addShotAndCreateRelation(bucketDB, shot))
                .doOnNext(this::updateBucketShotsCount)
                .toCompletable();
    }

    public Single<Bucket> createBucket(@NonNull String name, @Nullable String description) {
        Timber.d("Creating bucket: %s in local repository", name);
        return daoSession.getBucketDBDao().rx()
                .insertOrReplace(BucketDbMapper.createNewBucket(name, description))
                .map(Bucket::fromDB)
                .toSingle();
    }

    public Single<List<BucketWithShots>> getUserBucketsWithShots() {
        Timber.d("Getting buckets with shots from local repository");
        return daoSession.getBucketDBDao().queryBuilder().rx().oneByOne()
                .map(bucketDB -> BucketWithShots.create(Bucket.fromDB(bucketDB),
                        Shot.createListFromDB(bucketDB.getShots())))
                .toList().toSingle();
    }

    public Completable removeBucket(long bucketId) {
        Timber.d("Removing bucket from local repository");
        return daoSession.rxTx().run(() -> {
            removeShotFromBucket(daoSession.load(BucketDB.class, bucketId).getShots());
            daoSession.getBucketDBDao().deleteByKey(bucketId);
        }).toCompletable();
    }

    public Single<List<Shot>> getShotsListFromBucket(long bucketId) {
        Timber.d("Getting shots list from bucket local repository");
        return daoSession.getBucketDBDao().queryBuilder()
                .where(BucketDBDao.Properties.Id.eq(bucketId))
                .rx()
                .unique()
                .flatMap(bucketDB -> {
                    if (bucketDB == null) {
                        return Observable.empty();
                    }
                    return Observable.just(Shot.createListFromDB(bucketDB.getShots()));
                })
                .toSingle();
    }

    public Completable isShotBucketed(long shotId) {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shotId))
                .rx()
                .unique()
                .flatMap(shot -> shot.getIsBucketed()
                        ? Observable.empty() : Observable.error(new Throwable(SHOT_IS_NOT_BUCKETED_ERROR)))
                .toCompletable();
    }

    private Observable<BucketDB> addShotAndCreateRelation(BucketDB bucketDB, Shot shot) {
        final ShotDB shotToAdd = ShotDBMapper.fromShot(shot);

        return daoSession.rxTx().run(() -> {
            daoSession.insertOrReplace(new JoinBucketsWithShots(null, bucketDB.getId(), shotToAdd.getId()));
            daoSession.insertOrReplace(shotToAdd);
        })
                .map(aVoid -> bucketDB)
                .doOnCompleted(() -> updateShot(shotToAdd));
    }

    private void removeShotFromBucket(List<ShotDB> bucketedShots) {
        for (ShotDB shot : bucketedShots) {
            shot.setBucketCount(shot.getBucketCount() - 1);
            if (shot.getBucketCount() == 0) {
                shot.setIsBucketed(false);
            }
            shot.update();
        }
    }

    private void updateShot(ShotDB shotDB) {
        shotDB.setIsBucketed(true);
        shotDB.setBucketCount(shotDB.getBucketCount() + 1);
        shotDB.update();
    }

    private void updateBucketShotsCount(BucketDB bucketDB) {
        bucketDB.setShotsCount(bucketDB.getShotsCount() + 1);
        bucketDB.update();
    }
}
