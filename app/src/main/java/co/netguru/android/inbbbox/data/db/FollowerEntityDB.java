package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.data.db.converters.ZonedDateTimeConverter;

@Entity
public class FollowerEntityDB {

    @Id
    private long id;
    @Convert(converter = ZonedDateTimeConverter.class, columnType = String.class)
    private ZonedDateTime createdAt;
    private long userId;
    @ToOne(joinProperty = "userId")
    private UserEntityDB user;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 979453088)
    private transient FollowerEntityDBDao myDao;

    @Generated(hash = 2020176733)
    public FollowerEntityDB(long id, ZonedDateTime createdAt, long userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    @Generated(hash = 1853705126)
    public FollowerEntityDB() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 921178723)
    public UserEntityDB getUser() {
        long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserEntityDBDao targetDao = daoSession.getUserEntityDBDao();
            UserEntityDB userNew = targetDao.load(__key);
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
    @Generated(hash = 745770947)
    public void setUser(@NotNull UserEntityDB user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            userId = user.getId();
            user__resolvedKey = userId;
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
    @Generated(hash = 1697135917)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFollowerEntityDBDao() : null;
    }

}
