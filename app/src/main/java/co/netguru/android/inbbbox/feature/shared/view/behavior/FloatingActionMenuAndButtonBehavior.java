package co.netguru.android.inbbbox.feature.shared.view.behavior;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Behavior which makes clans fab move up when snackbar is shown
 */
@SuppressWarnings("unused")
public class FloatingActionMenuAndButtonBehavior extends CoordinatorLayout.Behavior {

    private float mTranslationY;

    public FloatingActionMenuAndButtonBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (child instanceof FloatingActionButton || child instanceof FloatingActionMenu && dependency instanceof Snackbar.SnackbarLayout) {
            this.updateTranslation(parent, child, dependency);
        }
        return false;
    }

    private void updateTranslation(CoordinatorLayout parent, View child, View dependency) {
        float translationY = this.getTranslationY(parent, child);
        if (Float.compare(translationY, mTranslationY) != 0) {
            ViewCompat.animate(child)
                    .cancel();
            if (Float.compare(Math.abs(translationY - this.mTranslationY), (float) dependency.getHeight()) == 0) {
                ViewCompat.animate(child)
                        .translationY(translationY)
                        .setListener(null);
            } else {
                ViewCompat.setTranslationY(child, translationY);
            }

            this.mTranslationY = translationY;
        }

    }

    private float getTranslationY(CoordinatorLayout parent, View child) {
        float minOffset = 0.0F;

        for (View view : parent.getDependencies(child)) {
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
            }
        }

        return minOffset;
    }
}