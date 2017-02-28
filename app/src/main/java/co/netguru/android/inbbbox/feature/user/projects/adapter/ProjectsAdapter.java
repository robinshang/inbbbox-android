package co.netguru.android.inbbbox.feature.user.projects.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.user.projects.ProjectsPresenter;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {

    private final OnGetMoreProjectShotsListener onGetMoreProjectShotsListener;
    private final ProjectClickListener projectClickListener;

    private List<ProjectWithShots> projectList;

    public ProjectsAdapter(@NonNull OnGetMoreProjectShotsListener onGetMoreProjectShotsListener,
                           @NonNull ProjectClickListener projectClickListener) {
        this.onGetMoreProjectShotsListener = onGetMoreProjectShotsListener;
        this.projectClickListener = projectClickListener;
        projectList = Collections.emptyList();
    }

    @Override
    public ProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProjectsViewHolder(parent, onGetMoreProjectShotsListener, projectClickListener);
    }

    @Override
    public void onBindViewHolder(ProjectsViewHolder holder, int position) {
        holder.bind(projectList.get(position));
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void setProjectList(List<ProjectWithShots> projectList) {
        this.projectList = projectList;
        notifyDataSetChanged();
    }

    public void addMoreProjects(List<ProjectWithShots> projects) {
        final int currentSize = this.projectList.size();
        this.projectList.addAll(projects);
        notifyItemRangeChanged(currentSize - 1, projects.size());
    }

    public List<ProjectWithShots> getProjectList() {
        return projectList;
    }

    public void addMoreProjectShots(long projectId, List<Shot> shotList) {
        final int index = findProjectIndex(projectId);
        final ProjectWithShots project = projectList.get(index);
        project.shotList().addAll(shotList);
        updateProjectShotPageStatus(index, project, shotList.size() >= ProjectsPresenter.PROJECT_SHOTS_PAGE_COUNT);

        notifyItemChanged(index);
    }

    private int findProjectIndex(long projectId) {
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).id() == projectId) {
                return i;
            }
        }
        throw new IllegalArgumentException("There is no project with id: " + projectId);
    }

    private void updateProjectShotPageStatus(int index, ProjectWithShots project, boolean hasMoreShots) {
        projectList.set(index, ProjectWithShots.update(project, hasMoreShots));
    }

    @FunctionalInterface
    public interface OnGetMoreProjectShotsListener {

        void onGetMoreProjectShots(ProjectWithShots projectWithShots);
    }
}
