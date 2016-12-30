package co.netguru.android.inbbbox.data.follower.controllers;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerGuest implements FollowersController {

    private final GuestModeFollowersRepository guestModeFollowersRepository;
    private final FollowersApi followersApi;

    public FollowersControllerGuest(GuestModeFollowersRepository guestModeFollowersRepository,
                                    FollowersApi followersApi) {
        this.guestModeFollowersRepository = guestModeFollowersRepository;
        this.followersApi = followersApi;
    }

    @Override
    public Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount) {
        return getFollowersFromApi(pageNumber, pageCount)
                .flatMap(Observable::from)
                .mergeWith(guestModeFollowersRepository.getFollowers());
    }

    @Override
    public Completable unFollowUser(long id) {
        return guestModeFollowersRepository.removeFollower(id);
    }

    @Override
    public Completable followUser(long id) {
        // TODO: 22.12.2016 Refactor adding follower to db
        return Completable.complete();
    }

    @Override
    public Single<Boolean> isUserFollowed(long id) {
        // TODO: 22.12.2016 Ask database instead of dribbble API
        return Single.just(false);
    }

    private Observable<List<FollowerEntity>> getFollowersFromApi(int pageNumber, int pageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount);
    }
}
