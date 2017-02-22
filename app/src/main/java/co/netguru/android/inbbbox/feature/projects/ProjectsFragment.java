package co.netguru.android.inbbbox.feature.projects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;

public class ProjectsFragment extends BaseMvpFragment<ProjectsContract.View, ProjectsContract.Presenter>
        implements ProjectsContract.View {

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 22.02.2017 Create proper layout
        return inflater.inflate(R.layout.fragment_buckets, container, false);
    }

    @NonNull
    @Override
    public ProjectsContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusProjectsComponent().getPresenter();
    }
}
