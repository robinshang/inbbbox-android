package co.netguru.android.inbbbox.model.localrepository.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinShotsWithBuckets {

    @Id(autoincrement = true)
    private Long id;
    private long shotId;
    private long bucketId;

    @Generated(hash = 1442967674)
    public JoinShotsWithBuckets(Long id, long shotId, long bucketId) {
        this.id = id;
        this.shotId = shotId;
        this.bucketId = bucketId;
    }

    @Generated(hash = 1333144005)
    public JoinShotsWithBuckets() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getShotId() {
        return this.shotId;
    }

    public void setShotId(long shotId) {
        this.shotId = shotId;
    }

    public long getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }
}
