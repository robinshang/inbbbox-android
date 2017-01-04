package co.netguru.android.inbbbox.data.dribbbleuser.user;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.data.db.UserDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;

@AutoValue
public abstract class User implements Parcelable {
    public abstract long id();

    public abstract String name();

    public abstract String avatarUrl();

    public abstract String username();

    public abstract int shotsCount();

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

        public abstract User build();
    }

    public static User create(UserEntity entity) {
        return User.builder()
                .id(entity.id())
                .name(entity.username())
                .avatarUrl(entity.avatarUrl())
                .username(entity.username())
                .shotsCount(entity.shotsCount())
                .build();
    }

    public static User fromDB(UserDB userDB) {
        return User.builder()
                .id(userDB.getId())
                .name(userDB.getName())
                .avatarUrl(userDB.getAvatarUrl())
                .username(userDB.getUsername())
                .shotsCount(userDB.getShotsCount())
                .build();
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }
}
