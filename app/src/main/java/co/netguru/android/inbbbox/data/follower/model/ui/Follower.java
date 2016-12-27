package co.netguru.android.inbbbox.data.follower.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

@AutoValue
public abstract class Follower implements Parcelable {

    public abstract long id();

    public abstract String name();

    public abstract String username();

    public abstract String avatarUrl();

    public abstract int shotsCount();

    @Nullable
    public abstract List<Shot> shotList();

    public static Builder builder() {
        return new AutoValue_Follower.Builder();
    }

    public static Follower create(FollowerEntity followerEntity, List<Shot> shotList) {
        return Follower.builder()
                .id(followerEntity.user().id())
                .name(followerEntity.user().name())
                .username(followerEntity.user().username())
                .avatarUrl(followerEntity.user().avatarUrl())
                .shotsCount(followerEntity.user().shotsCount())
                .shotList(shotList)
                .build();
    }

    public static Follower createFromUser(User user, List<Shot> shotList) {
        return Follower.builder()
                .id(user.id())
                .name(user.name())
                .username(user.username())
                .avatarUrl(user.avatarUrl())
                .shotsCount(user.shotsCount())
                .shotList(shotList)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder username(String username);

        public abstract Builder avatarUrl(String avatarUrl);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder shotList(List<Shot> shotList);

        public abstract Follower build();
    }
}
