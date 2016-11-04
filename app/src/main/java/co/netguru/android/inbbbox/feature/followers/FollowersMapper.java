package co.netguru.android.inbbbox.feature.followers;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.models.FollowerEntity;
import co.netguru.android.inbbbox.data.ui.Follower;

@FragmentScope
public class FollowersMapper {

    @Inject
    FollowersMapper() {

    }

    public Follower toFollower(FollowerEntity followerEntity) {
        return Follower.builder()
                .id(followerEntity.user().getId())
                .name(followerEntity.user().getName())
                .username(followerEntity.user().getUsername())
                .avatarUrl(followerEntity.user().getAvatarUrl())
                .shotsCount(followerEntity.user().getShotsCount())
                .build();
    }
}
