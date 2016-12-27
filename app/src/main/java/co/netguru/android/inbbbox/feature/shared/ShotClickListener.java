package co.netguru.android.inbbbox.feature.shared;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

@FunctionalInterface
public interface ShotClickListener {
    void onShotClick(Shot shot);
}