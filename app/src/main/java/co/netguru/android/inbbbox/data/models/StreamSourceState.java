package co.netguru.android.inbbbox.data.models;

public class StreamSourceState {
    private Boolean followingState;
    private Boolean newTodayState;
    private Boolean popularTodayState;
    private Boolean debut;

    public Boolean getFollowingState() {
        return followingState;
    }

    public void setFollowingState(Boolean followingState) {
        this.followingState = followingState;
    }

    public Boolean getNewTodayState() {
        return newTodayState;
    }

    public void setNewTodayState(Boolean newTodayState) {
        this.newTodayState = newTodayState;
    }

    public Boolean getPopularTodayState() {
        return popularTodayState;
    }

    public void setPopularTodayState(Boolean popularTodayState) {
        this.popularTodayState = popularTodayState;
    }

    public Boolean getDebut() {
        return debut;
    }

    public void setDebut(Boolean debut) {
        this.debut = debut;
    }
}
