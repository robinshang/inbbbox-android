package co.netguru.android.inbbbox.data.follower.controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.BaseGuestModeRepository;
import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.db.FollowerEntityDBDao;
import co.netguru.android.inbbbox.data.db.mappers.FollowerEntityDBMapper;
import co.netguru.android.inbbbox.data.db.mappers.UserEntityDBMapper;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;
import rx.Single;
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

    public Single<Boolean> isUserFollowed(long id) {
        Timber.d("Checking if user is follower");
        return daoSession.getFollowerEntityDBDao().queryBuilder()
                .where(FollowerEntityDBDao.Properties.Id.eq(id))
                .rx()
                .unique()
                .map(followerEntityDB -> followerEntityDB != null)
                .toSingle();
    }
}
