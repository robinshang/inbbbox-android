package co.netguru.android.inbbbox.model.localrepository.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeamDB {

    @Id
    private long id;
    private String name;

    @Generated(hash = 269872159)
    public TeamDB(long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 61943165)
    public TeamDB() {
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
}
