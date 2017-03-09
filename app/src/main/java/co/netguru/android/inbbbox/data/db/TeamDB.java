package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class TeamDB {

    @Id
    private Long id;
    private String name;
    private int shotsCount;
    private int bucketsCount;
    private int projectsCount;

    @Generated(hash = 1493083191)
    public TeamDB(Long id, String name, int shotsCount, int bucketsCount,
                  int projectsCount) {
        this.id = id;
        this.name = name;
        this.shotsCount = shotsCount;
        this.bucketsCount = bucketsCount;
        this.projectsCount = projectsCount;
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
}
