package co.netguru.android.inbbbox.localrepository.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;

@Singleton
public class GuestModeBucketsRepository {

    private final DaoSession daoSession;

    @Inject
    public GuestModeBucketsRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
