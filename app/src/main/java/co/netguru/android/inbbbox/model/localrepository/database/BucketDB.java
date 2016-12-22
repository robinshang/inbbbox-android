package co.netguru.android.inbbbox.model.localrepository.database;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import co.netguru.android.inbbbox.model.localrepository.database.converter.LocalDateTimeConverter;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class BucketDB {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String description;
    private int shotsCount;
    // TODO: 22.12.2016 Change to ZonedDateTime
    @Convert(converter = LocalDateTimeConverter.class, columnType = String.class)
    private LocalDateTime createdAt;
    @ToMany
    @JoinEntity(entity = JoinBucketsWithShots.class, sourceProperty = "bucketId", targetProperty = "shotId")
    private List<ShotDB> shots;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 270209034)
    private transient BucketDBDao myDao;

    @Generated(hash = 1831703667)
    public BucketDB(Long id, String name, String description, int shotsCount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shotsCount = shotsCount;
        this.createdAt = createdAt;
    }

    @Generated(hash = 1798362414)
    public BucketDB() {
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShotsCount() {
        return this.shotsCount;
    }

    public void setShotsCount(int shotsCount) {
        this.shotsCount = shotsCount;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 128790285)
    public List<ShotDB> getShots() {
        if (shots == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ShotDBDao targetDao = daoSession.getShotDBDao();
            List<ShotDB> shotsNew = targetDao._queryBucketDB_Shots(id);
            synchronized (this) {
                if (shots == null) {
                    shots = shotsNew;
                }
            }
        }
        return shots;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 80505623)
    public synchronized void resetShots() {
        shots = null;
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
    @Generated(hash = 1346736713)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBucketDBDao() : null;
    }
}
