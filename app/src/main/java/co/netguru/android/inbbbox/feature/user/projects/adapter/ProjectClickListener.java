package co.netguru.android.inbbbox.feature.user.projects.adapter;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;

public interface ProjectClickListener {
    void onProjectClick(ProjectWithShots projectWithShots);
    void onShotClick(Shot shot, ProjectWithShots projectWithShots);
}
