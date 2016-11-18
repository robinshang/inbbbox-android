package co.netguru.android.inbbbox.model.api;

import com.google.gson.annotations.SerializedName;

public class TeamEntity {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("bio")
    private String bio;
    @SerializedName("location")
    private String location;
    @SerializedName("links")
    private Links links;
    @SerializedName("buckets_count")
    private Integer bucketsCount;
    @SerializedName("comments_received_count")
    private Integer commentsReceivedCount;
    @SerializedName("followers_count")
    private Integer followersCount;
    @SerializedName("followings_count")
    private Integer followingsCount;
    @SerializedName("likes_count")
    private Integer likesCount;
    @SerializedName("likes_received_count")
    private Integer likesReceivedCount;
    @SerializedName("members_count")
    private Integer membersCount;
    @SerializedName("projects_count")
    private Integer projectsCount;
    @SerializedName("rebounds_received_count")
    private Integer reboundsReceivedCount;
    @SerializedName("shots_count")
    private Integer shotsCount;
    @SerializedName("can_upload_shot")
    private Boolean canUploadShot;
    @SerializedName("type")
    private String type;
    @SerializedName("pro")
    private Boolean pro;
    @SerializedName("buckets_url")
    private String bucketsUrl;
    @SerializedName("followers_url")
    private String followersUrl;
    @SerializedName("following_url")
    private String followingUrl;
    @SerializedName("likes_url")
    private String likesUrl;
    @SerializedName("members_url")
    private String membersUrl;
    @SerializedName("shots_url")
    private String shotsUrl;
    @SerializedName("team_shots_url")
    private String teamShotsUrl;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Integer getBucketsCount() {
        return bucketsCount;
    }

    public void setBucketsCount(Integer bucketsCount) {
        this.bucketsCount = bucketsCount;
    }

    public Integer getCommentsReceivedCount() {
        return commentsReceivedCount;
    }

    public void setCommentsReceivedCount(Integer commentsReceivedCount) {
        this.commentsReceivedCount = commentsReceivedCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(Integer followingsCount) {
        this.followingsCount = followingsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getLikesReceivedCount() {
        return likesReceivedCount;
    }

    public void setLikesReceivedCount(Integer likesReceivedCount) {
        this.likesReceivedCount = likesReceivedCount;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }

    public Integer getReboundsReceivedCount() {
        return reboundsReceivedCount;
    }

    public void setReboundsReceivedCount(Integer reboundsReceivedCount) {
        this.reboundsReceivedCount = reboundsReceivedCount;
    }

    public Integer getShotsCount() {
        return shotsCount;
    }

    public void setShotsCount(Integer shotsCount) {
        this.shotsCount = shotsCount;
    }

    public Boolean getCanUploadShot() {
        return canUploadShot;
    }

    public void setCanUploadShot(Boolean canUploadShot) {
        this.canUploadShot = canUploadShot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPro() {
        return pro;
    }

    public void setPro(Boolean pro) {
        this.pro = pro;
    }

    public String getBucketsUrl() {
        return bucketsUrl;
    }

    public void setBucketsUrl(String bucketsUrl) {
        this.bucketsUrl = bucketsUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public void setLikesUrl(String likesUrl) {
        this.likesUrl = likesUrl;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }

    public String getShotsUrl() {
        return shotsUrl;
    }

    public void setShotsUrl(String shotsUrl) {
        this.shotsUrl = shotsUrl;
    }

    public String getTeamShotsUrl() {
        return teamShotsUrl;
    }

    public void setTeamShotsUrl(String teamShotsUrl) {
        this.teamShotsUrl = teamShotsUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
