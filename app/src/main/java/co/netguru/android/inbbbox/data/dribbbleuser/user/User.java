package co.netguru.android.inbbbox.data.dribbbleuser.user;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.data.db.UserDB;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;

@AutoValue
public abstract class User implements Parcelable {

    public static final String TYPE_SINGLE_USER = "Player";
    public static final String TYPE_TEAM = "Team";

    public abstract long id();

    public abstract String name();

    public abstract String avatarUrl();

    public abstract String username();

    public abstract int shotsCount();

    public abstract String type();

    public abstract int followersCount();

    public abstract int followingsCount();

    public abstract int bucketsCount();

    public abstract int projectsCount();

    public abstract String bio();

    public abstract String location();

    public abstract Links links();

    public static User.Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract User.Builder id(long id);

        public abstract User.Builder name(String name);

        public abstract User.Builder avatarUrl(String url);

        public abstract User.Builder username(String username);

        public abstract User.Builder shotsCount(int shotsCount);

        public abstract User.Builder type(String type);

        public abstract User.Builder followersCount(int followersCount);

        public abstract User.Builder followingsCount(int followingsCount);

        public abstract User.Builder bucketsCount(int bucketsCount);

        public abstract User.Builder projectsCount(int projectsCount);

        public abstract User.Builder bio(String bio);

        public abstract User.Builder location(String location);

        public abstract User.Builder links(Links links);

        public abstract User build();
    }

    public static User create(UserEntity entity) {
        return User.builder()
                .id(entity.id())
                .name(entity.username())
                .avatarUrl(entity.avatarUrl())
                .username(entity.username())
                .shotsCount(entity.shotsCount())
                .type(entity.type())
                .followersCount(entity.followersCount())
                .followingsCount(entity.followingsCount())
                .bucketsCount(entity.bucketsCount())
                .projectsCount(entity.projectsCount())
                .bio(entity.bio())
                .location(entity.location() == null ? "Unknown" : entity.location())
                .links(entity.links())
                .build();
    }

    public static User createFromTeam(Team team) {
        return User.builder()
                .id(team.id())
                .name(team.name())
                .avatarUrl(team.avatarUrl())
                .username(team.username())
                .shotsCount(team.shotsCount())
                .bucketsCount(team.bucketsCount())
                .projectsCount(team.projectsCount())
                .type(TYPE_TEAM)
                .build();
    }

    public static User fromDB(UserDB userDB) {
        return User.builder()
                .id(userDB.getId())
                .name(userDB.getName())
                .avatarUrl(userDB.getAvatarUrl())
                .username(userDB.getUsername())
                .shotsCount(userDB.getShotsCount())
                .projectsCount(userDB.getProjectsCount())
                .bucketsCount(userDB.getBucketsCount())
                .followersCount(userDB.getFollowersCount())
                .followingsCount(userDB.getFollowingsCount())
                .bio(userDB.getBio())
                .location(userDB.getLocation())
                .type(userDB.getType())
                .links(Links.create(userDB.getLinks().getWeb(), userDB.getLinks().getTwitter()))
                .build();
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }
}
