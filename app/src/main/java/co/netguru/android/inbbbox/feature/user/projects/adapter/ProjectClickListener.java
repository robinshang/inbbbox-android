package co.netguru.android.inbbbox.feature.user.projects.adapter;

import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;

@FunctionalInterface
public interface ProjectClickListener {
    void onProjectClick(ProjectWithShots projectWithShots);
}
