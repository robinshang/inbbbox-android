package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import co.netguru.android.inbbbox.model.api.UserEntity;

@AutoValue
public abstract class User implements Parcelable{
    public abstract long id();

    public abstract String name();

    public abstract String avatarUrl();

    public static User.Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract User.Builder id(long id);

        public abstract User.Builder name(String name);

        public abstract User.Builder avatarUrl(String url);

        public abstract User build();
    }

    public static User create(UserEntity entity) {
        return User.builder()
                .id(entity.id())
                .name(entity.username())
                .avatarUrl(entity.avatarUrl())
                .build();
    }
}