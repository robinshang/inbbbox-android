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
        return shotDBDao.rx().insertOrReplace(ShotDBMapper.fromShot(shot))
                .flatMap(shotDB -> storeUser(shot.author()))
                .flatMap(userDB -> storeTeam(shot.team()))
                .toCompletable();
    }

    public Observable<List<Shot>> getLikedShots() {
        return shotDBDao.queryBuilder().rx().oneByOne().map(Shot::fromDB).toList();
    }

    private Observable<UserDB> storeUser(User user) {
        return userDBDao.rx().insertOrReplace(UserDBMapper.fromUser(user));
    }

    private Observable<TeamDB> storeTeam(Team team) {
        if (team != null) {
            return teamDBDao.rx().insertOrReplace(TeamDBMapper.fromTeam(team));
        }
        return Observable.empty();
    }
}
