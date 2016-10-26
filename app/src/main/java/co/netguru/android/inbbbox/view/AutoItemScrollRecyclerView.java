package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class AutoItemScrollRecyclerView extends RecyclerView {
    private int lastVisibleItemIndex;
    private int firstVisibleItemIndex;
    private boolean isScrollingNext;
    private boolean isScrollingBack;

    public AutoItemScrollRecyclerView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setNestedScrollingEnabled(false);

    }

    public AutoItemScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoItemScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && isScrollingNext) {
            // special handler to avoid displaying half elements
            scrollToNext();
        } else if (state == RecyclerView.SCROLL_STATE_IDLE && isScrollingBack) {
            scrollToBack();
        }
    }

    private void scrollToBack() {
        getFirstAndLastItemPosition();
        super.smoothScrollToPosition(firstVisibleItemIndex);
    }

    private void scrollToNext() {
        getFirstAndLastItemPosition();
        super.smoothScrollToPosition(lastVisibleItemIndex);
    }

    private void getFirstAndLastItemPosition() {
        firstVisibleItemIndex = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        lastVisibleItemIndex = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (dy > 0) {
            isScrollingNext = true;
            isScrollingBack = false;
        } else if (dy < 0) {
            isScrollingNext = false;
            isScrollingBack = true;
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        //fling disabled
        return super.fling(0, 0);
    }
}
