package co.netguru.android.inbbbox.feature.projects.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.projects.model.ui.Project;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

public class ProjectsViewHolder extends BaseViewHolder<Project> {

    ProjectsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_list_item, parent, false));
    }

    @Override
    public void bind(Project item) {

    }
}
