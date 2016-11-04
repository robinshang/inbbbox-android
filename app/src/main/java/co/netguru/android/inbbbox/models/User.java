package co.netguru.android.inbbbox.models;

import android.os.Build;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class User {

    @SerializedName("id")
    public abstract Integer id();

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

    @SerializedName("location")
    public abstract String location();

    @SerializedName("links")
    public abstract Links links();

    @SerializedName("buckets_count")
    public abstract Integer bucketsCount();

    @SerializedName("comments_received_count")
    public abstract Integer commentsReceivedCount();

    @SerializedName("followers_count")
    public abstract Integer followersCount();

    @SerializedName("followings_count")
    public abstract Integer followingsCount();

    @SerializedName("likes_count")
    public abstract Integer likesCount();

    @SerializedName("likes_received_count")
    public abstract Integer likesReceivedCount();

    @SerializedName("projects_count")
    public abstract Integer projectsCount();

    @SerializedName("rebounds_received_count")
    public abstract Integer reboundsReceivedCount();

    @SerializedName("shots_count")
    public abstract Integer shotsCount();

    @SerializedName("teams_count")
    public abstract Integer teamsCount();

    @SerializedName("can_upload_shot")
    public abstract Boolean canUploadShot();

    @SerializedName("type")
    public abstract String type();

    @SerializedName("pro")
    public abstract Boolean pro();

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

    @SerializedName("teams_url")
    public abstract String teamsUrl();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("updated_at")
    public abstract String updatedAt();

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder name(String name);

        public abstract Builder username(String username);

        public abstract Builder htmlUrl(String htmlUrl);

        public abstract Builder avatarUrl(String avatarUrl);

        public abstract Builder bio(String bio);

        public abstract Builder location(String location);

        public abstract Builder links(Links links);

        public abstract Builder bucketsCount(Integer bucketsCount);

        public abstract Builder commentsReceivedCount(Integer commentsReceivedCount);

        public abstract Builder followersCount(Integer followersCount);

        public abstract Builder followingsCount(Integer followingsCount);

        public abstract Builder likesCount(Integer likesCount);

        public abstract Builder likesReceivedCount(Integer likesReceivedCount);

        public abstract Builder projectsCount(Integer projectsCount);

        public abstract Builder reboundsReceivedCount(Integer reboundsReceivedCount);

        public abstract Builder shotsCount(Integer shotsCount);

        public abstract Builder teamsCount(Integer teamsCount);

        public abstract Builder canUploadShot(Boolean canUploadShot);

        public abstract Builder type(String type);

        public abstract Builder pro(Boolean pro);

        public abstract Builder bucketsUrl(String bucketsUrl);

        public abstract Builder followersUrl(String followersUrl);

        public abstract Builder followingUrl(String followingUrl);

        public abstract Builder likesUrl(String likes);

        public abstract Builder shotsUrl(String shotsUrl);

        public abstract Builder teamsUrl(String teamsUrl);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder updatedAt(String updatedAt);

        public abstract User build();
    }
}
