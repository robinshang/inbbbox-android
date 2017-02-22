package co.netguru.android.inbbbox.feature.user.projects.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {

    private List<ProjectWithShots> projectList;

    public ProjectsAdapter() {
        projectList = Collections.emptyList();
    }

    @Override
    public ProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProjectsViewHolder(parent);
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

    public List<ProjectWithShots> getProjectList() {
        return projectList;
    }
}
