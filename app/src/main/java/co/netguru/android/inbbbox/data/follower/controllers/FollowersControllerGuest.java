package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerGuest implements FollowersController {

    private static final int FIRST_PAGE = 1;

    private final GuestModeFollowersRepository guestModeFollowersRepository;
    private final FollowersApi followersApi;

    public FollowersControllerGuest(GuestModeFollowersRepository guestModeFollowersRepository,
                                    FollowersApi followersApi) {
        this.guestModeFollowersRepository = guestModeFollowersRepository;
        this.followersApi = followersApi;
    }

    @Override
    public Observable<User> getFollowedUsers(int pageNumber, int pageCount, int followerShotPageCount) {
        if (pageNumber == FIRST_PAGE) {
            return guestModeFollowersRepository.getFollowers()
                    .mergeWith(getFollowersFromApi(FIRST_PAGE, pageCount));
        }
        return getFollowersFromApi(pageNumber, pageCount);
    }

    @Override
    public Completable unFollowUser(long id) {
        return guestModeFollowersRepository.removeFollower(id);
    }

    @Override
    public Completable followUser(User follower) {
        return guestModeFollowersRepository.addFollower(follower);
    }

    @Override
    public Single<Boolean> isUserFollowed(long id) {
        return guestModeFollowersRepository.isUserFollowed(id);
    }

    private Observable<User> getFollowersFromApi(int pageNumber, int pageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(followerEntity -> User.create(followerEntity.user(), null));
    }
}
