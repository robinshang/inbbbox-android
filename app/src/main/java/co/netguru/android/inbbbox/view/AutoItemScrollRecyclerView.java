package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class AutoItemScrollRecyclerView extends RecyclerView {
    private boolean isScrollingNext;
    private boolean isScrollingBack;
    private int currentItem;
    private int orientation;

    public AutoItemScrollRecyclerView(Context context) {
        super(context);
        init();
    }

    public AutoItemScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoItemScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        currentItem = 0;
        setNestedScrollingEnabled(false);
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
        int firstVisibleItemIndex = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        super.smoothScrollToPosition(firstVisibleItemIndex);
        currentItem = firstVisibleItemIndex;
    }

    private void scrollToNext() {
        int lastVisibleItemIndex = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        super.smoothScrollToPosition(lastVisibleItemIndex);
        currentItem = lastVisibleItemIndex;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if ((orientation == VERTICAL && dy > 0) || (orientation == HORIZONTAL && dx > 0)) {
            isScrollingNext = true;
            isScrollingBack = false;
        } else if ((orientation == VERTICAL && dy < 0) || (orientation == HORIZONTAL && dx < 0)) {
            isScrollingNext = false;
            isScrollingBack = true;
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        //fling disabled
        return false;
    }

    public int getCurrentItem() {
        return getChildCount() == 0 ? RecyclerView.NO_POSITION : currentItem;
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            super.setLayoutManager(layoutManager);
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else {
            throw new RuntimeException("Layout manager should be instance of LinearLayoutManager");
        }
    }

}
