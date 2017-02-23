package co.netguru.android.inbbbox.feature.user.projects.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.user.projects.adapter.shots.ProjectShotsAdapter;

public class ProjectsViewHolder extends BaseViewHolder<ProjectWithShots> {

    private static final int SHOTS_TO_LOAD_MORE = 10;

    @BindView(R.id.project_item_header)
    TextView headerTextView;
    @BindView(R.id.project_item_small_header)
    TextView smallHeaderTextView;
    @BindView(R.id.project_item_shot_count)
    TextView shotCountTextView;
    @BindView(R.id.project_item_recycler_view)
    RecyclerView recyclerView;

    private final ProjectsAdapter.OnGetMoreProjectShotsListener onGetMoreProjectShotsListener;

    private ProjectShotsAdapter adapter;

    @Nullable
    private ProjectWithShots currentItem;

    ProjectsViewHolder(ViewGroup parent, ProjectsAdapter.OnGetMoreProjectShotsListener onGetMoreProjectShotsListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_item, parent, false));
        this.onGetMoreProjectShotsListener = onGetMoreProjectShotsListener;
        initializeRecyclerView(parent.getContext());
    }

    @Override
    public void bind(ProjectWithShots item) {
        currentItem = item;
        headerTextView.setText(item.name());
        smallHeaderTextView.setText(item.name());
        shotCountTextView.setText(String.valueOf(item.shotsCount()));
        adapter.setShotList(item.shotList());
    }

    private void initializeRecyclerView(Context context) {
        adapter = new ProjectShotsAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                if (currentItem != null && currentItem.hasMoreShots()) {
                    onGetMoreProjectShotsListener.onGetMoreProjectShots(currentItem);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
