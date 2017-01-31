package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.github.clans.fab.FloatingActionMenu;

import co.netguru.android.inbbbox.R;

public class FogFloatingActionMenu extends FloatingActionMenu {

    private static final long FOG_ANIM_DURATION = 200;
    private static final int FAB_MAIN_BUTTON_INDEX = 4;

    private View fogView;
    private boolean isInStaticMode;

    public FogFloatingActionMenu(Context context) {
        super(context);
        initFabMenu();
    }

    public FogFloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFabMenu();
    }

    public FogFloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFabMenu();
    }

    public void addFogView(View fogView) {

        this.fogView = fogView;
    }

    public void setOrientation(int orientation) {
        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            enableStaticMode();
        } else {
            enableDynamicMode();
        }
    }

    @Override
    public void close(boolean animate) {
        if (!isInStaticMode) {
            super.close(animate);
        }
    }

    /**
     * Menu is always opened and large FAB is hidden
     */
    private void enableStaticMode() {
        isInStaticMode = true;
        open(false);
        post(this::hideDynamicItems);
    }

    private void enableDynamicMode() {
        isInStaticMode = false;
    }

    private void initFabMenu() {
        setOnMenuToggleListener(this::handleFabMenuToggle);
    }

    private void handleFabMenuToggle(boolean opened) {
        if (opened) {
            showFog();
            animateMenuButtonOpen();
        } else {
            hideFog();
            animateMenuButtonClose();
        }
    }

    private void animateMenuButtonClose() {
        setMenuButtonColorNormal(ContextCompat.getColor(getContext(), R.color.secondaryWindowBackground));
        getMenuIconView().setImageResource(R.drawable.ic_fab_open);
    }

    private void animateMenuButtonOpen() {
        setMenuButtonColorNormal(ContextCompat.getColor(getContext(), R.color.accent));
        getMenuIconView().setImageResource(R.drawable.ic_fab_close);
    }

    private void hideFog() {
        if (fogView != null && !isInStaticMode) {
            fogView
                    .animate()
                    .alpha(0)
                    .setDuration(FOG_ANIM_DURATION)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> fogView.setVisibility(View.GONE));
        }
    }

    private void showFog() {
        if (fogView != null && !isInStaticMode) {
            fogView
                    .animate()
                    .alpha(1)
                    .setDuration(FOG_ANIM_DURATION)
                    .setInterpolator(new AccelerateInterpolator())
                    .withStartAction(() -> fogView.setVisibility(View.VISIBLE));
        }
    }

    private void hideDynamicItems() {
        for (int i = FAB_MAIN_BUTTON_INDEX; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.clearAnimation();
            child.setVisibility(GONE);
        }
    }
}
