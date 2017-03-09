package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean canScrollVertically = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setCanScrollVertically(boolean canScrollVertically) {
        this.canScrollVertically = canScrollVertically;
    }

    @Override
    public boolean canScrollVertically() {
        return canScrollVertically && super.canScrollVertically();
    }
}
