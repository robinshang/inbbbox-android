package co.netguru.android.inbbbox.feature.user.projects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.user.projects.adapter.ProjectsAdapter;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;

public class ProjectsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<ProjectWithShots>,
        ProjectsContract.View, ProjectsContract.Presenter> implements ProjectsContract.View,
        ProjectsAdapter.OnGetMoreProjectShotsListener {

    private static final String USER_KEY = "userKey";

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.projects_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;

    @BindColor(R.color.accent)
    int accentColor;

    private ProjectsAdapter projectsAdapter;

    public static ProjectsFragment newInstance(UserWithShots user) {
        final Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        final ProjectsFragment fragment = new ProjectsFragment();
        fragment.setArguments(args);

        return fragment;
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
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::getUserProjects);
    }

    private void initRecyclerView() {
        projectsAdapter = new ProjectsAdapter(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(projectsAdapter);
    }

    @NonNull
    @Override
    public LceViewState<List<ProjectWithShots>, ProjectsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<ProjectWithShots> getData() {
        return projectsAdapter.getProjectList();
    }

    @Override
    public void setData(List<ProjectWithShots> data) {
        projectsAdapter.setProjectList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().userDataReceived(getArguments().getParcelable(USER_KEY));
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addMoreProjectShots(long projectId, List<Shot> shotList) {
        projectsAdapter.addMoreProjectShots(projectId, shotList);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showTextOnSnackbar(errorText);
    }

    @Override
    public void onGetMoreProjectShots(ProjectWithShots project) {
        getPresenter().getMoreShotsFromProject(project);
    }
}
