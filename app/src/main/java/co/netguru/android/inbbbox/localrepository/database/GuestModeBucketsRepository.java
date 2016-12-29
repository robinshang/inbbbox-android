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
import co.netguru.android.inbbbox.model.localrepository.database.JoinBucketsWithShotsDao;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.BucketDbMapper;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
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
        Timber.d("Adding shot to bucket from local repository");
        return daoSession.getBucketDBDao().queryBuilder()
                .where(BucketDBDao.Properties.Id.eq(bucketId))
                .rx()
                .unique()
                .flatMap(bucketDB -> addShotAndCreateRelation(bucketDB, shot))
                .doOnNext(this::increaseBucketShotsCount)
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
            updateBucketShots(bucketDB.getShots(), bucketId);
            bucketDB.delete();
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

    public Single<Boolean> isShotBucketed(long shotId) {
        Timber.d("Checking if shot is bucketed");
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shotId))
                .rx()
                .unique()
                .map(shotDB -> !(shotDB == null || shotDB.getBuckets().isEmpty()))
                .toSingle();
    }

    private Observable<BucketDB> addShotAndCreateRelation(BucketDB bucketDB, Shot shot) {
        return daoSession.rxTx().run(() -> {
            insertUserIfExists(shot);
            final ShotDB shotToAdd = createShotToAdd(shot);
            daoSession.insertOrReplace(new JoinBucketsWithShots(null, bucketDB.getId(), shotToAdd.getId()));
            daoSession.insertOrReplace(shotToAdd);
        }).map(aVoid -> bucketDB);
    }

    private void updateBucketShots(List<ShotDB> bucketedShots, long bucketId) {
        removeBucketShotsRelations(bucketId);
        for (final ShotDB shot : bucketedShots) {
            shot.setBucketCount(shot.getBucketCount() - 1);
            shot.update();
            if (shot.getBuckets().isEmpty() && !shot.getIsLiked()) {
                shot.delete();
            }
        }
    }

    private void removeBucketShotsRelations(long bucketId) {
        final List<JoinBucketsWithShots> bucketWithShots = daoSession.getJoinBucketsWithShotsDao()
                .queryBuilder()
                .where(JoinBucketsWithShotsDao.Properties.BucketId.eq(bucketId))
                .list();
        for (final JoinBucketsWithShots bucketWithShot : bucketWithShots) {
            daoSession.delete(bucketWithShot);
        }
    }

    private ShotDB createShotToAdd(Shot shot) {
        final ShotDB shotToAdd = getNewOrExistingShot(shot);
        shotToAdd.setBucketCount(shotToAdd.getBucketCount() + 1);

        return shotToAdd;
    }

    private void increaseBucketShotsCount(BucketDB bucketDB) {
        bucketDB.setShotsCount(bucketDB.getShotsCount() + 1);
        bucketDB.update();
    }
}
