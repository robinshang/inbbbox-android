package co.netguru.android.inbbbox.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private final int launchWhenLastXVisible;

    private boolean scrolled;

    public LoadMoreScrollListener(int launchWhenLastXVisible) {
        this.launchWhenLastXVisible = launchWhenLastXVisible;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            scrolled = true;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        final LinearLayoutManager linearLayoutManager = getLayoutManager(recyclerView);
        final int totalItemCount = linearLayoutManager.getItemCount();
        final int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

        if (scrolled && totalItemCount - lastVisibleItemPosition < launchWhenLastXVisible) {
            scrolled = false;
            requestMoreData();
        }
    }

    private LinearLayoutManager getLayoutManager(RecyclerView recyclerView) {
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new RuntimeException("Layout manager should be instance of LinearLayoutManager");
        }
        return (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public abstract void requestMoreData();
}