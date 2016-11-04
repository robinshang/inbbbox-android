package co.netguru.android.inbbbox.data.ui;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Follower {

    public abstract int id();

    public abstract String name();

    public abstract String username();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    public static Builder builder() {
        return new AutoValue_Follower.Builder();
    }

    public static TypeAdapter<Follower> typeAdapter(Gson gson) {
        return new AutoValue_Follower.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int id);

        public abstract Builder name(String name);

        public abstract Builder username(String username);

        public abstract Builder avatarUrl(String avatarUrl);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Follower build();
    }
}
