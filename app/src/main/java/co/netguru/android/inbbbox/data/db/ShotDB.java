package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.data.db.converters.ZonedDateTimeConverter;

@Entity
public class ShotDB {

    @Id
    private Long id;
    private String title;
    @Convert(converter = ZonedDateTimeConverter.class, columnType = String.class)
    private ZonedDateTime creationDate;
    private String projectUrl;
    private int likesCount;
    private int bucketCount;
    private int commentsCount;
    private String description;
    private boolean isGif;
    private String hiDpiImageUrl;
    private String normalImageUrl;
    private String thumbnailUrl;
    private boolean isBucketed;
    private boolean isLiked;
    private Long userId;
    private Long teamId;
    @ToOne(joinProperty = "userId")
    private UserDB user;
    @ToOne(joinProperty = "teamId")
    private TeamDB team;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 163997700)
    private transient ShotDBDao myDao;

    @Generated(hash = 1054056122)
    public ShotDB(Long id, String title, ZonedDateTime creationDate, String projectUrl, int likesCount,
                  int bucketCount, int commentsCount, String description, boolean isGif, String hiDpiImageUrl,
                  String normalImageUrl, String thumbnailUrl, boolean isBucketed, boolean isLiked, Long userId,
                  Long teamId) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.projectUrl = projectUrl;
        this.likesCount = likesCount;
        this.bucketCount = bucketCount;
        this.commentsCount = commentsCount;
        this.description = description;
        this.isGif = isGif;
        this.hiDpiImageUrl = hiDpiImageUrl;
        this.normalImageUrl = normalImageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.isBucketed = isBucketed;
        this.isLiked = isLiked;
        this.userId = userId;
        this.teamId = teamId;
    }

    @Generated(hash = 335679081)
    public ShotDB() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getProjectUrl() {
        return this.projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getBucketCount() {
        return this.bucketCount;
    }

    public void setBucketCount(int bucketCount) {
        this.bucketCount = bucketCount;
    }

    public int getCommentsCount() {
        return this.commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsGif() {
        return this.isGif;
    }

    public void setIsGif(boolean isGif) {
        this.isGif = isGif;
    }

    public String getHiDpiImageUrl() {
        return this.hiDpiImageUrl;
    }

    public void setHiDpiImageUrl(String hiDpiImageUrl) {
        this.hiDpiImageUrl = hiDpiImageUrl;
    }

    public String getNormalImageUrl() {
        return this.normalImageUrl;
    }

    public void setNormalImageUrl(String normalImageUrl) {
        this.normalImageUrl = normalImageUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean getIsBucketed() {
        return this.isBucketed;
    }

    public void setIsBucketed(boolean isBucketed) {
        this.isBucketed = isBucketed;
    }

    public boolean getIsLiked() {
        return this.isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1144150912)
    public UserDB getUser() {
        Long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDBDao targetDao = daoSession.getUserDBDao();
            UserDB userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 711898738)
    public void setUser(UserDB user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }

    @Generated(hash = 1834174654)
    private transient Long team__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1111803104)
    public TeamDB getTeam() {
        Long __key = this.teamId;
        if (team__resolvedKey == null || !team__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeamDBDao targetDao = daoSession.getTeamDBDao();
            TeamDB teamNew = targetDao.load(__key);
            synchronized (this) {
                team = teamNew;
                team__resolvedKey = __key;
            }
        }
        return team;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1476213970)
    public void setTeam(TeamDB team) {
        synchronized (this) {
            this.team = team;
            teamId = team == null ? null : team.getId();
            team__resolvedKey = teamId;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 907843013)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getShotDBDao() : null;
    }
}
