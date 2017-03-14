package co.netguru.android.inbbbox.data.dribbbleuser.team;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import co.netguru.android.inbbbox.data.dribbbleuser.Links;

@AutoValue
public abstract class TeamEntity implements Parcelable {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("username")
    public abstract String username();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();

    @SerializedName("bio")
    public abstract String bio();

    @SerializedName("links")
    public abstract Links links();

    @SerializedName("buckets_count")
    public abstract int bucketsCount();

    @SerializedName("projects_count")
    public abstract int projectsCount();

    @SerializedName("followers_count")
    public abstract int followersCount();

    @SerializedName("followings_count")
    public abstract int followingsCount();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    @SerializedName("type")
    public abstract String type();

    @SerializedName("pro")
    public abstract boolean pro();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("updated_at")
    public abstract String updatedAt();

    public static TypeAdapter<TeamEntity> typeAdapter(Gson gson) {
        return new AutoValue_TeamEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_TeamEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder username(String username);

        public abstract Builder avatarUrl(String avatarUrl);

        public abstract Builder bio(String bio);

        public abstract Builder links(Links links);

        public abstract Builder bucketsCount(int bucketsCount);

        public abstract Builder projectsCount(int projectsCount);

        public abstract Builder followersCount(int followersCount);

        public abstract Builder followingsCount(int followingsCount);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder type(String type);

        public abstract Builder pro(boolean pro);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder updatedAt(String updatedAt);

        public abstract TeamEntity build();
    }
}