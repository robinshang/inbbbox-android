package co.netguru.android.inbbbox.feature.projects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.projects.model.ui.Project;
import co.netguru.android.inbbbox.feature.projects.adapter.ProjectsAdapter;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;

public class ProjectsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Project>,
        ProjectsContract.View, ProjectsContract.Presenter> implements ProjectsContract.View {

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.projects_recycler_view)
    RecyclerView recyclerView;

    @BindColor(R.color.accent)
    int accentColor;

    private ProjectsAdapter projectsAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initSwipeRefreshView();
    }

    @NonNull
    @Override
    public ProjectsContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusProjectsComponent().getPresenter();
    }

    private void initSwipeRefreshView() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // TODO: 22.02.2017 Call proper method from presenter
        });
    }

    private void initRecyclerView() {
        projectsAdapter = new ProjectsAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(projectsAdapter);
    }

    @NonNull
    @Override
    public LceViewState<List<Project>, ProjectsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Project> getData() {
        return projectsAdapter.getProjectList();
    }

    @Override
    public void setData(List<Project> data) {
        projectsAdapter.setProjectList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getUserProjects(1);
    }
}
