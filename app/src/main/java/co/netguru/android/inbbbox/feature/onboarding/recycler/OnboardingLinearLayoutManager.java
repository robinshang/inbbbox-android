package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnboardingLinearLayoutManager extends LinearLayoutManager {

    private boolean canScroll = false;

    public OnboardingLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, int position) {
        canScroll = true;
        super.smoothScrollToPosition(recyclerView, state, position);
    }

    @Override
    public boolean canScrollVertically() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}
