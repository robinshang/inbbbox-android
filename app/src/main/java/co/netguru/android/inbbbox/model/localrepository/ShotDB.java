package co.netguru.android.inbbbox.model.localrepository;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShotDB {

    @Id
    private long id;

    @Generated(hash = 1446168908)
    public ShotDB(long id) {
        this.id = id;
    }

    @Generated(hash = 335679081)
    public ShotDB() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
