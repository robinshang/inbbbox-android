package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity
public class TeamDB {

    @Id
    private Long id;
    private String name;
    private String username;
    private String avatarUrl;
    private int shotsCount;
    private int followersCount;
    private int followingsCount;
    private int bucketsCount;
    private int projectsCount;
    private String bio;
    private String location;
    @ToOne(joinProperty = "id")
    private LinksDB links;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 321862286)
    private transient TeamDBDao myDao;

    @Generated(hash = 1607522451)
    public TeamDB(Long id, String name, String username, String avatarUrl,
                  int shotsCount, int followersCount, int followingsCount,
                  int bucketsCount, int projectsCount, String bio, String location) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.shotsCount = shotsCount;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
        this.bucketsCount = bucketsCount;
        this.projectsCount = projectsCount;
        this.bio = bio;
        this.location = location;
    }

    @Generated(hash = 61943165)
    public TeamDB() {
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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getShotsCount() {
        return this.shotsCount;
    }

    public void setShotsCount(int shotsCount) {
        this.shotsCount = shotsCount;
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

    @Generated(hash = 1078680006)
    private transient Long links__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 225080737)
    public LinksDB getLinks() {
        Long __key = this.id;
        if (links__resolvedKey == null || !links__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LinksDBDao targetDao = daoSession.getLinksDBDao();
            LinksDB linksNew = targetDao.load(__key);
            synchronized (this) {
                links = linksNew;
                links__resolvedKey = __key;
            }
        }
        return links;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1731259973)
    public void setLinks(LinksDB links) {
        synchronized (this) {
            this.links = links;
            id = links == null ? null : links.getId();
            links__resolvedKey = id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 175223458)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeamDBDao() : null;
    }
}
