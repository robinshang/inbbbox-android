package co.netguru.android.inbbbox.models;

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

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }
}
