package co.netguru.android.inbbbox.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Links{

    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("twitter")
    @Expose
    private String twitter;

    /**
     *
     * @return
     * The web
     */
    public String getWeb() {
        return web;
    }

    /**
     *
     * @param web
     * The web
     */
    public void setWeb(String web) {
        this.web = web;
    }

    /**
     *
     * @return
     * The twitter
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     *
     * @param twitter
     * The twitter
     */
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

}
