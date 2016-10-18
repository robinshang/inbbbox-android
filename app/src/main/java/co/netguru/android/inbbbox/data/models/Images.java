package co.netguru.android.inbbbox.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Images implements Serializable{

    @SerializedName("hidpi")
    @Expose
    private Object hidpi;
    @SerializedName("normal")
    @Expose
    private String normal;
    @SerializedName("teaser")
    @Expose
    private String teaser;

    /**
     *
     * @return
     * The hidpi
     */
    public Object getHidpi() {
        return hidpi;
    }

    /**
     *
     * @param hidpi
     * The hidpi
     */
    public void setHidpi(Object hidpi) {
        this.hidpi = hidpi;
    }

    /**
     *
     * @return
     * The normal
     */
    public String getNormal() {
        return normal;
    }

    /**
     *
     * @param normal
     * The normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
    }

    /**
     *
     * @return
     * The teaser
     */
    public String getTeaser() {
        return teaser;
    }

    /**
     *
     * @param teaser
     * The teaser
     */
    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

}