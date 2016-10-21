package co.netguru.android.inbbbox.data.models;

public class StreamSourceState {
    private boolean followingState;
    private boolean newTodayState;
    private boolean popularTodayState;
    private boolean debut;

    public boolean getFollowingState() {
        return followingState;
    }

    public void setFollowingState(boolean followingState) {
        this.followingState = followingState;
    }

    public boolean getNewTodayState() {
        return newTodayState;
    }

    public void setNewTodayState(boolean newTodayState) {
        this.newTodayState = newTodayState;
    }

    public boolean getPopularTodayState() {
        return popularTodayState;
    }

    public void setPopularTodayState(boolean popularTodayState) {
        this.popularTodayState = popularTodayState;
    }

    public boolean getDebut() {
        return debut;
    }

    public void setDebut(boolean debut) {
        this.debut = debut;
    }
}
