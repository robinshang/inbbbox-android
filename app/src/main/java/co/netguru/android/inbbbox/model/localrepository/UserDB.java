package co.netguru.android.inbbbox.model.localrepository;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserDB {

    @Id
    private long id;
    private String name;
    private String avatarUrl;
    private String username;
    private int shotsCount;
    @Generated(hash = 1250734072)
    public UserDB(long id, String name, String avatarUrl, String username,
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
