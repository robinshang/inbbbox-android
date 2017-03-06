package co.netguru.android.inbbbox.feature.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.shotsadapter.SharedShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;

@FragmentScope
public class ProjectFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<Shot>,
        ProjectContract.View, ProjectContract.Presenter> implements ProjectContract.View {

    public static final String TAG = ProjectFragment.class.getSimpleName();
    private static final String KEY_PROJECT = "key:project";
    private static final int SPAN_COUNT = 2;
    private static final int LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE = 10;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private SharedShotsAdapter shotsAdapter;
    private ProjectShotClickListener projectShotClickListener;

    public static ProjectFragment newInstance(ProjectWithShots projectWithShots) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PROJECT, projectWithShots);

        ProjectFragment projectFragment = new ProjectFragment();
        projectFragment.setArguments(args);

        return projectFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            projectShotClickListener = (ProjectShotClickListener) context;
        } catch (ClassCastException e) {
            throw new InterfaceNotImplementedException(e, context.toString(),
                    ShotClickListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        initRefreshLayout();
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        shotsAdapter.setGridMode(isGridMode);
    }

    @Override
    public LceViewState<List<Shot>, ProjectContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return shotsAdapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        shotsAdapter.setShots(data);
    }

    @Override
    public void addShots(List<Shot> shots) {
        shotsAdapter.addNewShots(shots);
    }

    @Override
    public void showMessageOnServerError(String message) {
        showTextOnSnackbar(message);
    }

    @Override
    public void showLoadingMoreShotsView() {
        showTextOnSnackbar(R.string.loading_more_shots);
    }

    @Override
    public void showShotDetails(Shot shot) {
        projectShotClickListener.onShotDetailsRequest(shot, shotsAdapter.getData());
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public ProjectContract.Presenter createPresenter() {
        ProjectModule module = new ProjectModule(getArguments().getParcelable(KEY_PROJECT));
        return App.getUserComponent(getContext()).plusProjectComponent(module).getPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().refreshShots();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshShots);
    }

    private LoadMoreScrollListener getScrollListener() {
        return new LoadMoreScrollListener(LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.loadMoreShots();
            }
        };
    }

    private void initRecycler() {
        gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        linearLayoutManager = new LinearLayoutManager(getContext());
        shotsAdapter = new SharedShotsAdapter(getPresenter()::onShotClick, null, null);
        recyclerView.setAdapter(shotsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(getScrollListener());
    }
}
