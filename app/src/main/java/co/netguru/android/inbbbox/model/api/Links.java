package co.netguru.android.inbbbox.model.api;

import com.google.gson.annotations.SerializedName;


public class Links {

    @SerializedName("web")
    private String web;
    @SerializedName("twitter")
    private String twitter;

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

}
