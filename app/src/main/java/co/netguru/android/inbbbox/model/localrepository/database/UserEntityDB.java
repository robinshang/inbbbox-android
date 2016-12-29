package co.netguru.android.inbbbox.model.localrepository.database;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.model.localrepository.database.converter.ZonedDateTimeConverter;

@Entity
public class UserEntityDB {

    @Id
    private long id;
    private String name;
    private String username;
    private String htmlUrl;
    private String avatarUrl;
    private String bio;
    @Nullable
    private String location;
    private String linksWeb;
    private String linksTwitter;
    private int bucketsCount;
    private int commentsReceivedCount;
    private int followersCount;
    private int followingsCount;
    private int likesCount;
    private int likesReceivedCount;
    private int projectsCount;
    private int reboundsReceivedCount;
    private int shotsCount;
    private int teamsCount;
    private boolean canUploadShot;
    private String type;
    private boolean pro;
    private String bucketsUrl;
    private String followersUrl;
    private String followingUrl;
    private String likesUrl;
    private String shotsUrl;
    private String teamsUrl;
    @Convert(converter = ZonedDateTimeConverter.class, columnType = String.class)
    private ZonedDateTime createdAt;
    @Convert(converter = ZonedDateTimeConverter.class, columnType = String.class)
    private ZonedDateTime updatedAt;

    @Generated(hash = 1459562272)
    public UserEntityDB(long id, String name, String username, String htmlUrl, String avatarUrl, String bio,
            String location, String linksWeb, String linksTwitter, int bucketsCount, int commentsReceivedCount,
            int followersCount, int followingsCount, int likesCount, int likesReceivedCount, int projectsCount,
            int reboundsReceivedCount, int shotsCount, int teamsCount, boolean canUploadShot, String type, boolean pro,
            String bucketsUrl, String followersUrl, String followingUrl, String likesUrl, String shotsUrl, String teamsUrl,
            ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.htmlUrl = htmlUrl;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.location = location;
        this.linksWeb = linksWeb;
        this.linksTwitter = linksTwitter;
        this.bucketsCount = bucketsCount;
        this.commentsReceivedCount = commentsReceivedCount;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
        this.likesCount = likesCount;
        this.likesReceivedCount = likesReceivedCount;
        this.projectsCount = projectsCount;
        this.reboundsReceivedCount = reboundsReceivedCount;
        this.shotsCount = shotsCount;
        this.teamsCount = teamsCount;
        this.canUploadShot = canUploadShot;
        this.type = type;
        this.pro = pro;
        this.bucketsUrl = bucketsUrl;
        this.followersUrl = followersUrl;
        this.followingUrl = followingUrl;
        this.likesUrl = likesUrl;
        this.shotsUrl = shotsUrl;
        this.teamsUrl = teamsUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 279541092)
    public UserEntityDB() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLinksWeb() {
        return this.linksWeb;
    }

    public void setLinksWeb(String linksWeb) {
        this.linksWeb = linksWeb;
    }

    public String getLinksTwitter() {
        return this.linksTwitter;
    }

    public void setLinksTwitter(String linksTwitter) {
        this.linksTwitter = linksTwitter;
    }

    public int getBucketsCount() {
        return this.bucketsCount;
    }

    public void setBucketsCount(int bucketsCount) {
        this.bucketsCount = bucketsCount;
    }

    public int getCommentsReceivedCount() {
        return this.commentsReceivedCount;
    }

    public void setCommentsReceivedCount(int commentsReceivedCount) {
        this.commentsReceivedCount = commentsReceivedCount;
    }

    public int getFollowersCount() {
        return this.followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingsCount() {
        return this.followingsCount;
    }

    public void setFollowingsCount(int followingsCount) {
        this.followingsCount = followingsCount;
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getLikesReceivedCount() {
        return this.likesReceivedCount;
    }

    public void setLikesReceivedCount(int likesReceivedCount) {
        this.likesReceivedCount = likesReceivedCount;
    }

    public int getProjectsCount() {
        return this.projectsCount;
    }

    public void setProjectsCount(int projectsCount) {
        this.projectsCount = projectsCount;
    }

    public int getReboundsReceivedCount() {
        return this.reboundsReceivedCount;
    }

    public void setReboundsReceivedCount(int reboundsReceivedCount) {
        this.reboundsReceivedCount = reboundsReceivedCount;
    }

    public int getShotsCount() {
        return this.shotsCount;
    }

    public void setShotsCount(int shotsCount) {
        this.shotsCount = shotsCount;
    }

    public int getTeamsCount() {
        return this.teamsCount;
    }

    public void setTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
    }

    public boolean getCanUploadShot() {
        return this.canUploadShot;
    }

    public void setCanUploadShot(boolean canUploadShot) {
        this.canUploadShot = canUploadShot;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getPro() {
        return this.pro;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }

    public String getBucketsUrl() {
        return this.bucketsUrl;
    }

    public void setBucketsUrl(String bucketsUrl) {
        this.bucketsUrl = bucketsUrl;
    }

    public String getFollowersUrl() {
        return this.followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return this.followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getLikesUrl() {
        return this.likesUrl;
    }

    public void setLikesUrl(String likesUrl) {
        this.likesUrl = likesUrl;
    }

    public String getShotsUrl() {
        return this.shotsUrl;
    }

    public void setShotsUrl(String shotsUrl) {
        this.shotsUrl = shotsUrl;
    }

    public String getTeamsUrl() {
        return this.teamsUrl;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
