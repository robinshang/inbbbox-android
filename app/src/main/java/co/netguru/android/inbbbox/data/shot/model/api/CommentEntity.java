package co.netguru.android.inbbbox.data.shot.model.api;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;

public class CommentEntity {

    @SerializedName("id")
    private long id;
    @SerializedName("body")
    private String body;
    @SerializedName("likes_count")
    private Integer likesCount;
    @SerializedName("likes_url")
    private String likesUrl;
    @SerializedName("created_at")
    private ZonedDateTime createdAt;
    @SerializedName("updated_at")
    private ZonedDateTime updatedAt;
    @SerializedName("user")
    private UserEntity user;

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public void setLikesUrl(String likesUrl) {
        this.likesUrl = likesUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
