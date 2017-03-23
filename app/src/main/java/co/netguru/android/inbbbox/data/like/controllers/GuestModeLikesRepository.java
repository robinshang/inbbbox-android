package co.netguru.android.inbbbox.data.like.controllers;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.BaseGuestModeRepository;
import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.db.ShotDB;
import co.netguru.android.inbbbox.data.db.ShotDBDao;
import co.netguru.android.inbbbox.data.db.mappers.TeamDBMapper;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class GuestModeLikesRepository extends BaseGuestModeRepository {

    @Inject
    GuestModeLikesRepository(DaoSession daoSession) {
        super(daoSession);
    }

    public Completable addLikedShot(@NonNull Shot shot) {
        return daoSession.rxTx()
                .run(() -> {
                    daoSession.getShotDBDao().insertOrReplace(likeShot(shot));
                    insertUserIfExists(shot.author());
                    if (shot.team() != null) {
                        daoSession.getTeamDBDao().insertOrReplace(TeamDBMapper.fromTeam(shot.team()));
                    }
                })
                .toCompletable();
    }

    public Observable<List<Shot>> getLikedShots() {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.IsLiked.eq(Boolean.TRUE))
                .rx()
                .oneByOne()
                .map(Shot::fromDB)
                .toList();
    }

    public Completable removeLikedShot(Shot shot) {
        if (shot.isBucketed()) {
            return daoSession.getShotDBDao().rx()
                    .insertOrReplace(unlikeShot(shot)).toCompletable();
        }

        return daoSession.getShotDBDao().rx().deleteByKey(shot.id()).toCompletable();
    }

    public Single<Boolean> isShotLiked(Shot shot) {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shot.id()))
                .rx()
                .unique()
                .map(this::checkIfShotDbIsLiked)
                .toSingle();
    }

    private boolean checkIfShotDbIsLiked(ShotDB shotDB) {
        boolean isLiked = false;
        if (shotDB == null || !shotDB.getIsLiked())
            isLiked = false;

        else if (shotDB.getIsLiked())
            isLiked = true;

        return isLiked;
    }

    private ShotDB likeShot(Shot shot) {
        final ShotDB shotDB = getNewOrExistingShot(shot);
        shotDB.setLikesCount(shotDB.getLikesCount() + 1);
        shotDB.setIsLiked(true);

        return shotDB;
    }

    private ShotDB unlikeShot(Shot shot) {
        final ShotDB shotDB = getNewOrExistingShot(shot);
        shotDB.setLikesCount(shotDB.getLikesCount() - 1);
        shotDB.setIsLiked(false);

        return shotDB;
    }
}
