package co.netguru.android.inbbbox.models;

public class StreamSourceSettings {
    private boolean following;
    private boolean newToday;
    private boolean popularToday;
    private boolean debut;

    public StreamSourceSettings() {
        //following stream should be enabled by default
        this.following = true;
    }

    public StreamSourceSettings(boolean following, boolean newToday, boolean popularToday, boolean debut) {
        this.following = following;
        this.newToday = newToday;
        this.popularToday = popularToday;
        this.debut = debut;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isNewToday() {
        return newToday;
    }

    public boolean isPopularToday() {
        return popularToday;
    }

    public boolean isDebut() {
        return debut;
    }
}
