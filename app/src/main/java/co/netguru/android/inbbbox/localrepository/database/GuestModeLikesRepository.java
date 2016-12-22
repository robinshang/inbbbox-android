package co.netguru.android.inbbbox.localrepository.database;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.TeamDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.UserDBMapper;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;

@Singleton
public class GuestModeLikesRepository {

    private static final String SHOT_IS_NOT_LIKED_ERROR = "Shot is not liked";

    private final DaoSession daoSession;

    @Inject
    GuestModeLikesRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Completable addLikedShot(@NonNull Shot shot) {
        return daoSession.rxTx()
                .run(() -> {
                    daoSession.getShotDBDao().insertOrReplace(ShotDBMapper.fromShot(shot));
                    daoSession.getUserDBDao().insertOrReplace(UserDBMapper.fromUser(shot.author()));
                    if (shot.team() != null) {
                        daoSession.getTeamDBDao().insertOrReplace(TeamDBMapper.fromTeam(shot.team()));
                    }
                })
                .toCompletable();
    }

    public Observable<List<Shot>> getLikedShots() {
        return daoSession.getShotDBDao().queryBuilder().rx().oneByOne().map(Shot::fromDB).toList();
    }

    public Completable removeLikedShot(Shot shot) {
        return daoSession.getShotDBDao().rx().deleteByKey(shot.id()).toCompletable();
    }

    public Completable isShotLiked(Shot shot) {
        return daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shot.id()))
                .rx()
                .unique()
                .flatMap(shotDB -> {
                    if (shotDB == null) {
                        return Observable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR));
                    }
                    return Observable.empty();
                })
                .toCompletable();
    }
}
