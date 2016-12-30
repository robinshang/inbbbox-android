package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinBucketsWithShots {

    @Id(autoincrement = true)
    private Long id;
    private long bucketId;
    private long shotId;

    @Generated(hash = 1518059435)
    public JoinBucketsWithShots(Long id, long bucketId, long shotId) {
        this.id = id;
        this.bucketId = bucketId;
        this.shotId = shotId;
    }

    @Generated(hash = 835510296)
    public JoinBucketsWithShots() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public long getShotId() {
        return this.shotId;
    }

    public void setShotId(long shotId) {
        this.shotId = shotId;
    }

}
