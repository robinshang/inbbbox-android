package co.netguru.android.inbbbox.feature.user.projects;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.project.ProjectActivity;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.CollectionAdapter;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.CollectionClickListener;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import timber.log.Timber;

public class ProjectsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<ProjectWithShots>,
        ProjectsContract.View, ProjectsContract.Presenter> implements ProjectsContract.View,
        CollectionAdapter.OnGetMoreCollectionShotsListener<ProjectWithShots>,
        CollectionClickListener<ProjectWithShots> {

    private static final String USER_KEY = "userKey";

    private static final int PROJECTS_TO_LOAD_MORE = 10;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.projects_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    FrameLayout emptyView;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;

    @BindColor(R.color.accent)
    int accentColor;

    private CollectionAdapter<ProjectWithShots> collectionAdapter;

    private ShotActionListener shotActionListener;

    public static ProjectsFragment newInstance(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        final ProjectsFragment fragment = new ProjectsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "must implement OnFollowedShotActionListener");
            throw new InterfaceNotImplementedException(e, context.toString(), ShotActionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        shotActionListener = null;
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

    @NonNull
    @Override
    public LceViewState<List<ProjectWithShots>, ProjectsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<ProjectWithShots> getData() {
        return collectionAdapter.getCollectionsList();
    }

    @Override
    public void setData(List<ProjectWithShots> data) {
        collectionAdapter.setCollectionsList(data);
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
    public void addMoreProjectShots(long projectId, List<Shot> shotList, int shotsPerPage) {
        collectionAdapter.addMoreCollectionShots(projectId, shotList, shotsPerPage);
    }

    @Override
    public void showLoadingMoreShotsFromProjectView() {
        showTextOnSnackbar(R.string.loading_more_project_shots);
    }

    @Override
    public void addMoreProjects(List<ProjectWithShots> projects) {
        collectionAdapter.addMoreCollections(projects);
    }

    @Override
    public void showLoadingMoreProjectsView() {
        showTextOnSnackbar(R.string.loading_more_projects);
    }

    @Override
    public void showProjectDetails(ProjectWithShots projectWithShots) {
        ProjectActivity.startActivity(getContext(), projectWithShots);
    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> allShots) {
        shotActionListener.showShotDetails(shot, allShots, shot.author().id());
    }

    @Override
    public void showContent() {
        super.showContent();
        getPresenter().showContentForData(getData());
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showTextOnSnackbar(errorText);
    }

    @Override
    public void onGetMoreCollectionShots(ProjectWithShots project) {
        getPresenter().getMoreShotsFromProject(project);
    }

    private void initSwipeRefreshView() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::getUserProjects);
    }

    private void initRecyclerView() {
        collectionAdapter = new CollectionAdapter<>(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(PROJECTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreUserProjects();
            }
        });
        recyclerView.setAdapter(collectionAdapter);
    }

    @Override
    public void onCollectionClick(ProjectWithShots projectWithShots) {
        getPresenter().onProjectClick(projectWithShots);
    }

    @Override
    public void onShotClick(Shot shot, ProjectWithShots projectWithShots) {
        getPresenter().onShotClick(shot, projectWithShots);
    }

}
