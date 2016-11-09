package co.netguru.android.inbbbox.feature.followers;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.ui.Follower;
import rx.Observable;

@FragmentScope
public class FollowersProvider {

    private final FollowersApi followersApi;
    private final FollowersMapper followersMapper;

    @Inject
    FollowersProvider(FollowersApi followersApi, FollowersMapper followersMapper) {
        this.followersApi = followersApi;
        this.followersMapper = followersMapper;
    }

    public Observable<Follower> getFollowedUsers() {
        return followersApi.getFollowedUsers()
                .flatMap(Observable::from)
                .map(followersMapper::toFollower);
    }
}
