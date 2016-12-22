package co.netguru.android.inbbbox.controller.followers;

import java.util.List;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.localrepository.database.GuestModeFollowersRepository;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;

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

    private Observable<List<FollowerEntity>> getFollowersFromApi(int pageNumber, int pageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount);
    }
}
