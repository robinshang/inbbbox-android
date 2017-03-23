package co.netguru.android.inbbbox.data;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.db.ShotDB;
import co.netguru.android.inbbbox.data.db.ShotDBDao;
import co.netguru.android.inbbbox.data.db.mappers.ShotDBMapper;
import co.netguru.android.inbbbox.data.db.mappers.UserDBMapper;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;


public abstract class BaseGuestModeRepository {

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

    protected void insertUserIfExists(@Nullable User user) {
        if (user != null) {
            daoSession.getUserDBDao().insertOrReplace(UserDBMapper.fromUser(user));
        }
    }
}
