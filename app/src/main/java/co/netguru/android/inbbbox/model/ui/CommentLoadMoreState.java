package co.netguru.android.inbbbox.model.ui;

public class CommentLoadMoreState {
    private boolean isLoadMoreActive;

    private boolean isWaitingForUpdate;

    public CommentLoadMoreState() {
        isLoadMoreActive = false;
        isWaitingForUpdate = true;
    }

    public boolean isLoadMoreActive() {
        return isLoadMoreActive;
    }

    public void setLoadMoreActive(boolean loadMoreActive) {
        isLoadMoreActive = loadMoreActive;
    }

    public boolean isWaitingForUpdate() {
        return isWaitingForUpdate;
    }

    public void setWaitingForUpdate(boolean waitingForUpdate) {
        isWaitingForUpdate = waitingForUpdate;
    }
}
