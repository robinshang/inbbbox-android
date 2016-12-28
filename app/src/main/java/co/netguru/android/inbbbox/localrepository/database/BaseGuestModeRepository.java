package co.netguru.android.inbbbox.localrepository.database;


import android.support.annotation.NonNull;

import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.UserDBMapper;
import co.netguru.android.inbbbox.model.ui.Shot;

abstract class BaseGuestModeRepository {

    protected final DaoSession daoSession;

    protected BaseGuestModeRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @NonNull
    protected ShotDB getNewOrExistingShot(@NonNull Shot shot) {
        final ShotDB shotDB = daoSession.getShotDBDao().queryBuilder()
                .where(ShotDBDao.Properties.Id.eq(shot.id()))
                .unique();

        return shotDB != null ? shotDB : ShotDBMapper.fromShot(shot);
    }

    protected void insertUserIfExists(Shot shot) {
        if (shot.author() != null) {
            daoSession.getUserDBDao().insertOrReplace(UserDBMapper.fromUser(shot.author()));
        }
    }
}
