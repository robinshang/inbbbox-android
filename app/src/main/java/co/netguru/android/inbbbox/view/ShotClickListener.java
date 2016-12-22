package co.netguru.android.inbbbox.view;

import co.netguru.android.inbbbox.model.ui.Shot;

@FunctionalInterface
public interface ShotClickListener {
    void onShotClick(Shot shot);
}
