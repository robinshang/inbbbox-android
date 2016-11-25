package co.netguru.android.inbbbox.model.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@AutoValue
public abstract class UserEntity implements Parcelable{

    @SerializedName("id")
    public abstract long id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("username")
    public abstract String username();

    @SerializedName("html_url")
    public abstract String htmlUrl();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();

    @SerializedName("bio")
    public abstract String bio();

    /**
     * WARNING: undocumented api behaviour null might be returned for location
     */
    @SerializedName("location")
    @Nullable
    public abstract String location();

    @SerializedName("links")
    public abstract Links links();

    @SerializedName("buckets_count")
    public abstract int bucketsCount();

    @SerializedName("comments_received_count")
    public abstract int commentsReceivedCount();

    @SerializedName("followers_count")
    public abstract int followersCount();

    @SerializedName("followings_count")
    public abstract int followingsCount();

    @SerializedName("likes_count")
    public abstract int likesCount();

    @SerializedName("likes_received_count")
    public abstract int likesReceivedCount();

    @SerializedName("projects_count")
    public abstract int projectsCount();

    @SerializedName("rebounds_received_count")
    public abstract int reboundsReceivedCount();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    @SerializedName("teams_count")
    public abstract int teamsCount();

    @SerializedName("can_upload_shot")
    public abstract boolean canUploadShot();

    @SerializedName("type")
    public abstract String type();

    @SerializedName("pro")
    public abstract boolean pro();

    @SerializedName("buckets_url")
    public abstract String bucketsUrl();

    @SerializedName("followers_url")
    public abstract String followersUrl();

    @SerializedName("following_url")
    public abstract String followingUrl();

    @SerializedName("likes_url")
    public abstract String likesUrl();

    @SerializedName("shots_url")
    public abstract String shotsUrl();

    @Nullable
    @SerializedName("teams_url")
    public abstract String teamsUrl();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    @SerializedName("updated_at")
    public abstract LocalDateTime updatedAt();

    public static Builder builder() {
        return new AutoValue_UserEntity.Builder();
    }

    public static TypeAdapter<UserEntity> typeAdapter(Gson gson) {
        return new AutoValue_UserEntity.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder username(String username);

        public abstract Builder htmlUrl(String htmlUrl);

        public abstract Builder avatarUrl(String avatarUrl);

        public abstract Builder bio(String bio);

        public abstract Builder location(String location);

        public abstract Builder links(Links links);

        public abstract Builder bucketsCount(int bucketsCount);

        public abstract Builder commentsReceivedCount(int commentsReceivedCount);

        public abstract Builder followersCount(int followersCount);

        public abstract Builder followingsCount(int followingsCount);

        public abstract Builder likesCount(int likesCount);

        public abstract Builder likesReceivedCount(int likesReceivedCount);

        public abstract Builder projectsCount(int projectsCount);

        public abstract Builder reboundsReceivedCount(int reboundsReceivedCount);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder teamsCount(int teamsCount);

        public abstract Builder canUploadShot(boolean canUploadShot);

        public abstract Builder type(String type);

        public abstract Builder pro(boolean pro);

        public abstract Builder bucketsUrl(String bucketsUrl);

        public abstract Builder followersUrl(String followersUrl);

        public abstract Builder followingUrl(String followingUrl);

        public abstract Builder likesUrl(String likes);

        public abstract Builder shotsUrl(String shotsUrl);

        public abstract Builder teamsUrl(String teamsUrl);

        public abstract Builder createdAt(LocalDateTime createdAt);

        public abstract Builder updatedAt(LocalDateTime updatedAt);

        public abstract UserEntity build();
    }
}