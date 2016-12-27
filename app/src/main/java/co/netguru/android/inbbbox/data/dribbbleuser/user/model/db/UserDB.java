package co.netguru.android.inbbbox.data.dribbbleuser.user.model.db;

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

    @Generated(hash = 1788000037)
    public UserDB(Long id, String name, String avatarUrl, String username,
                  int shotsCount) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.shotsCount = shotsCount;
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

}
