package co.netguru.android.inbbbox.feature.followers;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.ui.Follower;

@FragmentScope
public class FollowersMapper {

    @Inject
    FollowersMapper() {

    }

    public Follower toFollower(FollowerEntity followerEntity) {
        return Follower.builder()
                .id(followerEntity.user().id())
                .name(followerEntity.user().name())
                .username(followerEntity.user().username())
                .avatarUrl(followerEntity.user().avatarUrl())
                .shotsCount(followerEntity.user().shotsCount())
                .build();
    }
}
