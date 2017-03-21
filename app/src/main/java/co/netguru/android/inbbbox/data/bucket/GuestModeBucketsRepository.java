package co.netguru.android.inbbbox.data.bucket;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.BaseGuestModeRepository;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.db.BucketDB;
import co.netguru.android.inbbbox.data.db.BucketDBDao;
import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.db.JoinBucketsWithShots;
import co.netguru.android.inbbbox.data.db.JoinBucketsWithShotsDao;
import co.netguru.android.inbbbox.data.db.ShotDB;
import co.netguru.android.inbbbox.data.db.ShotDBDao;
import co.netguru.android.inbbbox.data.db.mappers.BucketDbMapper;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class GuestModeBucketsRepository extends BaseGuestModeRepository {

    @Inject
    GuestModeBucketsRepository(DaoSession daoSession) {
        super(daoSession);
    }

    public Single<List<Bucket>> getUserBuckets() {
        Timber.d("Getting buckets from local repository");
        return daoSession.getBucketDBDao().queryBuilder().rx().oneByOne()
                .map(Bucket::fromDB).toList().toSingle();
    }

    public Completable addShotToBucket(long bucketId, Shot shot) {
        Timber.d("Adding shot to bucket in local repository");
        return getUniqueBucketFromDatabase(bucketId)
                .flatMap(bucketDB -> addShotAndCreateRelation(bucketDB, shot))
                .doOnNext(this::increaseBucketShotsCount)
                .toCompletable();
    }

    public Completable removeShotFromBucket(long bucketId, Shot shot) {
        Timber.d("Removing shot from bucket in local repository");
        return getUniqueShotFromDatabase(shot.id())
                .doOnNext(shotDB -> removeShotFromBucket(bucketId, shotDB))
                .flatMap(shotDB -> getUniqueBucketFromDatabase(bucketId))
                .doOnNext(this::decreaseBucketShotsCount)
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
            final BucketDB bucketDB = daoSession.load(BucketDB.class, bucketId);
            removeShotsFromBuckets(bucketDB.getShots(), bucketId);
            bucketDB.delete();
        }).toCompletable();
    }

    public Single<List<Shot>> getShotsListFromBucket(long bucketId) {
        Timber.d("Getting shots list from bucket local repository");
        return getUniqueBucketFromDatabase(bucketId)
                .flatMap(bucketDB -> {
                    if (bucketDB == null) {
                        return Observable.empty();
                    }
                    return Observable.just(Shot.createListFromDB(bucketDB.getShots()));
                })
                .toSingle();
    }

    public Single<Boolean> isShotBucketed(long shotId) {
        Timber.d("Checking if shot is bucketed");
        return getUniqueShotFromDatabase(shotId)
                .map(shotDB -> !(shotDB == null || shotDB.getBuckets().isEmpty()))
                .toSingle();
    }

    public Single<List<Bucket>> getBucketsListForShot(long shotId) {
        Timber.d("Getting buckets list for shot from local repository");
        return daoSession.getBucketDBDao()
                .queryBuilder()
                .rx()
                .oneByOne()
                .filter(bucketDB -> checkBucketContainsShot(bucketDB, shotId))
                .map(Bucket::fromDB)
                .toList()
                .toSingle();
    }

    private Observable<BucketDB> getUniqueBucketFromDatabase(long bucketId) {
        return daoSession.getBucketDBDao().queryBuilder()
                .where(BucketDBDao.Properties.Id.eq(bucketId))
                .rx()
                .unique();
    }

    private Observable<ShotDB> getUniqueShotFromDatabase(long shotId) {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shotId))
                .rx()
                .unique();
    }

    private Observable<BucketDB> addShotAndCreateRelation(BucketDB bucketDB, Shot shot) {
        return daoSession.rxTx().run(() -> {
            insertUserIfExists(shot.author());
            final ShotDB shotToAdd = createShotToAdd(shot);
            daoSession.insertOrReplace(new JoinBucketsWithShots(null, bucketDB.getId(), shotToAdd.getId()));
            daoSession.insertOrReplace(shotToAdd);
        }).map(aVoid -> bucketDB);
    }

    private void removeShotsFromBuckets(List<ShotDB> bucketedShots, long bucketId) {
        for (final ShotDB shot : bucketedShots) {
            removeShotFromBucket(bucketId, shot);
        }
    }

    private void removeShotFromBucket(long bucketId, ShotDB shot) {
        removeBucketShotRelations(bucketId, shot.getId());
        shot.setBucketCount(shot.getBucketCount() - 1);
        shot.update();
        if (shot.getBuckets().isEmpty() && !shot.getIsLiked()) {
            shot.delete();
        }
    }

    private void removeBucketShotRelations(long bucketId, long shotId) {
        final JoinBucketsWithShots joinBucketsWithShots = daoSession.getJoinBucketsWithShotsDao()
                .queryBuilder()
                .where(JoinBucketsWithShotsDao.Properties.BucketId.eq(bucketId))
                .where(JoinBucketsWithShotsDao.Properties.ShotId.eq(shotId))
                .unique();
        daoSession.delete(joinBucketsWithShots);
    }

    private ShotDB createShotToAdd(Shot shot) {
        final ShotDB shotToAdd = getNewOrExistingShot(shot);
        shotToAdd.setBucketCount(shotToAdd.getBucketCount() + 1);

        return shotToAdd;
    }

    private boolean checkBucketContainsShot(BucketDB bucketDB, long shotId) {
        for (final ShotDB shotDB : bucketDB.getShots()) {
            if (shotDB.getId().equals(shotId)) {
                return true;
            }
        }
        return false;
    }

    private void increaseBucketShotsCount(BucketDB bucketDB) {
        bucketDB.setShotsCount(bucketDB.getShotsCount() + 1);
        bucketDB.update();
    }

    private void decreaseBucketShotsCount(BucketDB bucketDB) {
        bucketDB.setShotsCount(bucketDB.getShotsCount() - 1);
        bucketDB.update();
    }
}
