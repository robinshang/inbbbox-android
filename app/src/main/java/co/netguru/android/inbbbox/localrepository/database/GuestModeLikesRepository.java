package co.netguru.android.inbbbox.localrepository.database;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.UserDB;
import co.netguru.android.inbbbox.model.localrepository.database.UserDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.TeamDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.UserDBMapper;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Completable;
import rx.Observable;

@Singleton
public class GuestModeLikesRepository {

    private static final String SHOT_IS_NOT_LIKED_ERROR = "Shot is not liked";

    private final ShotDBDao shotDBDao;
    private final UserDBDao userDBDao;
    private final TeamDBDao teamDBDao;

    @Inject
    GuestModeLikesRepository(ShotDBDao shotDBDao, UserDBDao userDBDao, TeamDBDao teamDBDao) {
        this.shotDBDao = shotDBDao;
        this.userDBDao = userDBDao;
        this.teamDBDao = teamDBDao;
    }

    public Completable addLikedShot(@NonNull Shot shot) {
        return shotDBDao.rx().insertOrReplaceInTx(ShotDBMapper.fromShot(shot))
                .flatMap(shotDB -> storeUser(shot.author()))
                .flatMap(userDB -> storeTeam(shot.team()))
                .toCompletable();
    }

    public Observable<List<Shot>> getLikedShots() {
        return shotDBDao.queryBuilder().rx().oneByOne().map(Shot::fromDB).toList();
    }

    public Completable removeLikedShot(Shot shot) {
        return shotDBDao.rx().deleteByKey(shot.id()).toCompletable();
    }

    public Completable isShotLiked(Shot shot) {
        return shotDBDao.queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shot.id()))
                .rx()
                .unique()
                .flatMap(shotDB -> {
                    if (shotDB == null) {
                        return Observable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR));
                    }
                    return Observable.empty();
                }).toCompletable();
    }

    private Observable<UserDB> storeUser(User user) {
        return userDBDao.rx()
                .insertOrReplaceInTx(UserDBMapper.fromUser(user))
                .map(users -> (UserDB) users[0]);
    }

    private Observable<TeamDB> storeTeam(Team team) {
        if (team != null) {
            return teamDBDao.rx()
                    .insertOrReplaceInTx(TeamDBMapper.fromTeam(team))
                    .map(teams -> (TeamDB) teams[0]);
        }
        return Observable.empty();
    }
}
