package co.netguru.android.inbbbox.feature.onboarding;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import co.netguru.android.inbbbox.feature.shared.view.AutoItemScrollRecyclerView;

public class CustomRecyclerView extends AutoItemScrollRecyclerView {

    public boolean disableTouch;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return disableTouch;
    }
}
