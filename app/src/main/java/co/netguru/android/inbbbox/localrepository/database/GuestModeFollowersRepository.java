package co.netguru.android.inbbbox.localrepository.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.FollowerEntityDBMapper;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.UserEntityDBMapper;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

@Singleton
public class GuestModeFollowersRepository extends BaseGuestModeRepository {

    @Inject
    GuestModeFollowersRepository(DaoSession daoSession) {
        super(daoSession);
    }

    public Observable<FollowerEntity> getFollowers() {
        Timber.d("Getting followers from local repository");
        return daoSession.getFollowerEntityDBDao().queryBuilder().rx().oneByOne().map(FollowerEntity::fromDB);
    }

    public Completable removeFollower(long id) {
        Timber.d("Removing follower from local repository");
        return daoSession.getFollowerEntityDBDao().rx().deleteByKey(id).toCompletable();
    }

    // TODO: 22.12.2016 Add follower when follow will be available
    public Completable addFollower(FollowerEntity followerEntity) {
        Timber.d("Adding follower to local repository");
        return daoSession.rxTx().run(() -> {
            daoSession.getFollowerEntityDBDao().insertOrReplace(FollowerEntityDBMapper.fromFollowerEntity(followerEntity));
            daoSession.getUserEntityDBDao().insertOrReplace(UserEntityDBMapper.fromUserEntity(followerEntity.user()));
        }).toCompletable();
    }
}
