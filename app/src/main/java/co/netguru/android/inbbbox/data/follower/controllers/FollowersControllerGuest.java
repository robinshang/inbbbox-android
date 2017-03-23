package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.GuestModeFollowersRepository;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerGuest extends BaseFollowersController implements FollowersController {

    private final GuestModeFollowersRepository guestModeFollowersRepository;

    public FollowersControllerGuest(GuestModeFollowersRepository guestModeFollowersRepository,
                                    FollowersApi followersApi, ShotsApi shotsApi) {
        super(followersApi, shotsApi);
        this.guestModeFollowersRepository = guestModeFollowersRepository;
    }

    @Override
    public Observable<UserWithShots> getFollowedUsers(int pageNumber, int pageCount,
                                                      int followerShotPageCount,
                                                      boolean canUseCacheForShots) {
        if (pageNumber == FIRST_PAGE) {
            return guestModeFollowersRepository.getFollowersWithoutShots()
                    .flatMap(user -> getFollowerWithShots(user, followerShotPageCount,
                            canUseCacheForShots));
        }
        return Observable.empty();
    }

    @Override
    public Completable unFollowUser(long id) {
        return guestModeFollowersRepository.removeFollower(id);
    }

    @Override
    public Completable followUser(User user) {
        return guestModeFollowersRepository.addFollower(user);
    }

    @Override
    public Single<Boolean> isUserFollowed(long id) {
        return guestModeFollowersRepository.isUserFollowed(id);
    }
}
