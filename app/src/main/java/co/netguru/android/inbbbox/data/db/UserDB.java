package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UserDB {

    @Id
    private Long id;
    private String name;
    private String avatarUrl;
    private String username;
    private int shotsCount;
    private int bucketsCount;
    private int projectsCount;
    private String type;

    @Generated(hash = 617444720)
    public UserDB(Long id, String name, String avatarUrl, String username,
                  int shotsCount, int bucketsCount, int projectsCount, String type) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.shotsCount = shotsCount;
        this.bucketsCount = bucketsCount;
        this.projectsCount = projectsCount;
        this.type = type;
    }

    @Generated(hash = 1312299826)
    public UserDB() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShotsCount() {
        return this.shotsCount;
    }

    public void setShotsCount(int shotsCount) {
        this.shotsCount = shotsCount;
    }

    public int getBucketsCount() {
        return this.bucketsCount;
    }

    public void setBucketsCount(int bucketsCount) {
        this.bucketsCount = bucketsCount;
    }

    public int getProjectsCount() {
        return this.projectsCount;
    }

    public void setProjectsCount(int projectsCount) {
        this.projectsCount = projectsCount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
