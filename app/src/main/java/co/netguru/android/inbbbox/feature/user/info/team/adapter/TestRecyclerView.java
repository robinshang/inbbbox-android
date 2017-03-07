package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import timber.log.Timber;

public class TestRecyclerView extends RecyclerView {
    public TestRecyclerView(Context context) {
        super(context);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Timber.d("RecyclerView requestDisallowInterceptTouchEvent: "+disallowIntercept);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean s = super.onInterceptTouchEvent(e);
        Timber.d("RecyclerView onInterceptTouchEvent: "+e.getAction()+", super: "+s);
        return s;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean s = super.onTouchEvent(e);
        Timber.d("RecyclerView onTouchEvent: "+e.getAction()+", super: "+s);
        return s;
    }
}
