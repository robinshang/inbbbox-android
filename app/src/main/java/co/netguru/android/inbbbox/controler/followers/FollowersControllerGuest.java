package co.netguru.android.inbbbox.controler.followers;


import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.localrepository.GuestModeFollowersRepository;
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
        return null;
    }

    @Override
    public Completable unFollowUser(long id) {
        return null;
    }
}
