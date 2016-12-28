package co.netguru.android.inbbbox.localrepository.database;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.TeamDBMapper;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;

@Singleton
public class GuestModeLikesRepository extends BaseGuestModeRepository {

    private static final String SHOT_IS_NOT_LIKED_ERROR = "Shot is not liked";

    @Inject
    GuestModeLikesRepository(DaoSession daoSession) {
        super(daoSession);
    }

    public Completable addLikedShot(@NonNull Shot shot) {
        return daoSession.rxTx()
                .run(() -> {
                    daoSession.getShotDBDao().insertOrReplace(likeShot(shot));
                    insertUserIfExists(shot);
                    if (shot.team() != null) {
                        daoSession.getTeamDBDao().insertOrReplace(TeamDBMapper.fromTeam(shot.team()));
                    }
                })
                .toCompletable();
    }

    public Observable<List<Shot>> getLikedShots() {
        return daoSession.getShotDBDao().queryBuilder().rx().oneByOne()
                .filter(ShotDB::getIsLiked)
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

    public Completable isShotLiked(Shot shot) {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shot.id()))
                .rx()
                .unique()
                .flatMap(shotDB -> {
                    if (shotDB == null || !shotDB.getIsLiked()) {
                        return Observable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR));
                    }
                    return Observable.empty();
                })
                .toCompletable();
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
