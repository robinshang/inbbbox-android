package co.netguru.android.inbbbox.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LinksDB {

    @Id
    private long id;
    private String web;
    private String twitter;

    @Generated(hash = 1220517194)
    public LinksDB(long id, String web, String twitter) {
        this.id = id;
        this.web = web;
        this.twitter = twitter;
    }

    @Generated(hash = 1404050992)
    public LinksDB() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWeb() {
        return this.web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
