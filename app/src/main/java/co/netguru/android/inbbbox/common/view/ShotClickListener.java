package co.netguru.android.inbbbox.common.view;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

@FunctionalInterface
public interface ShotClickListener {
    void onShotClick(Shot shot);
}
