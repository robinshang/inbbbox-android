package co.netguru.android.inbbbox.feature.user.projects.adapter.shots;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class ProjectShotsAdapter extends RecyclerView.Adapter<ProjectShotsViewHolder> {

    private List<Shot> shotList;

    public ProjectShotsAdapter() {
        shotList = Collections.emptyList();
    }

    @Override
    public ProjectShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProjectShotsViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ProjectShotsViewHolder holder, int position) {
        holder.bind(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    public void setShotList(List<Shot> shotList) {
        this.shotList = shotList;
        notifyDataSetChanged();
    }
}
