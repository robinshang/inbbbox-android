package co.netguru.android.inbbbox.model.localrepository.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeamDB {

    @Id
    private Long id;
    private String name;

    @Generated(hash = 1003723811)
    public TeamDB(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
