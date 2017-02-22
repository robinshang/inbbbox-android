package co.netguru.android.inbbbox.feature.projects.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.projects.model.ui.Project;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {

    private List<Project> projectList;

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

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
        notifyDataSetChanged();
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}
