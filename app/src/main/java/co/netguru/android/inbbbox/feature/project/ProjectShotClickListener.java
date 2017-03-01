package co.netguru.android.inbbbox.feature.project;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

@FunctionalInterface
public interface ProjectShotClickListener {
    void onShotDetailsRequest(Shot shot, List<Shot> allShots);
}
