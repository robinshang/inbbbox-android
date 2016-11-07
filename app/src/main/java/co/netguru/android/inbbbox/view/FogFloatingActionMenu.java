package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.github.clans.fab.FloatingActionMenu;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.utils.Constants;

public class FogFloatingActionMenu extends FloatingActionMenu {
    private View fogView;

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
        setMenuButtonColorNormal(ContextCompat.getColor(getContext(), R.color.white));
        getMenuIconView().setImageResource(R.drawable.ic_fab_open);
    }

    private void animateMenuButtonOpen() {
        setMenuButtonColorNormal(ContextCompat.getColor(getContext(), R.color.accent));
        getMenuIconView().setImageResource(R.drawable.ic_fab_close);
    }

    private void hideFog() {
        if (fogView != null) {
            fogView
                    .animate()
                    .alpha(0)
                    .setDuration(Constants.Animations.FOG_ANIM_DURATION)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> fogView.setVisibility(View.GONE));
        }
    }

    private void showFog() {
        if (fogView != null) {
            fogView
                    .animate()
                    .alpha(1)
                    .setDuration(Constants.Animations.FOG_ANIM_DURATION)
                    .setInterpolator(new AccelerateInterpolator())
                    .withStartAction(() -> fogView.setVisibility(View.VISIBLE));
        }
    }


    public void addFogView(View fogView) {

        this.fogView = fogView;
    }
}
