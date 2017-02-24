package co.netguru.android.inbbbox.data.dribbbleuser.user;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.data.db.UserDB;
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
                .build();
    }

    public static User createFromTeam(Team team) {
        return User.builder()
                .id(team.id())
                .name(team.name())
                .avatarUrl(team.avatarUrl())
                .username(team.username())
                .shotsCount(team.shotsCount())
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
                .type(userDB.getType())
                .build();
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }
}
