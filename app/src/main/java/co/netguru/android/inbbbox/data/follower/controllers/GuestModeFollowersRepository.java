package co.netguru.android.inbbbox.data.follower.controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.BaseGuestModeRepository;
import co.netguru.android.inbbbox.data.db.DaoSession;
import co.netguru.android.inbbbox.data.db.FollowerDBDao;
import co.netguru.android.inbbbox.data.db.mappers.FollowerDBMapper;
import co.netguru.android.inbbbox.data.db.mappers.UserDBMapper;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
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

    public Observable<UserWithShots> getFollowers() {
        Timber.d("Getting followers from local repository");
        return daoSession.getFollowerDBDao().queryBuilder()
                .rx()
                .oneByOne()
                .map(UserWithShots::fromFollowerDB);
    }

    public Completable removeFollower(long id) {
        Timber.d("Removing follower from local repository");
        return daoSession.getFollowerDBDao()
                .rx()
                .deleteByKey(id)
                .toCompletable();
    }

    public Completable addFollower(User user) {
        Timber.d("Adding follower to local repository");
        return daoSession.rxTx().run(() -> {
            daoSession.getFollowerDBDao().insertOrReplace(FollowerDBMapper.fromUser(user));
            daoSession.getUserDBDao().insertOrReplace(UserDBMapper.fromUser(user));
        }).toCompletable();
    }

    public Single<Boolean> isUserFollowed(long id) {
        Timber.d("Checking if user is follower");
        return daoSession.getFollowerDBDao().queryBuilder()
                .where(FollowerDBDao.Properties.Id.eq(id))
                .rx()
                .unique()
                .map(followerEntityDB -> followerEntityDB != null)
                .toSingle();
    }
}
