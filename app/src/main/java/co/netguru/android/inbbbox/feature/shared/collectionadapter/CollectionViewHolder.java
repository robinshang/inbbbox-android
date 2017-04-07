package co.netguru.android.inbbbox.feature.shared.collectionadapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.shots.CollectionShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;

class CollectionViewHolder extends BaseViewHolder<ShotsCollection> {

    private static final int SHOTS_TO_LOAD_MORE = 10;
    private final CollectionAdapter.OnGetMoreCollectionShotsListener onGetMoreCollectionShotsListener;
    private final CollectionClickListener collectionClickListener;
    @BindView(R.id.project_item_header)
    TextView headerTextView;
    @BindView(R.id.project_item_small_header)
    TextView smallHeaderTextView;
    @BindView(R.id.project_item_shot_count)
    TextView shotCountTextView;
    @BindView(R.id.project_item_recycler_view)
    RecyclerView recyclerView;
    private CollectionShotsAdapter adapter;

    @Nullable
    private ShotsCollection currentItem;

    CollectionViewHolder(ViewGroup parent, CollectionAdapter.OnGetMoreCollectionShotsListener onGetMoreProjectShotsListener,
                         CollectionClickListener projectClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_item, parent, false));
        this.onGetMoreCollectionShotsListener = onGetMoreProjectShotsListener;
        this.collectionClickListener = projectClickListener;
        initializeRecyclerView(parent.getContext());
    }

    @OnClick(R.id.project_item_small_header)
    void onProjectClick() {
        collectionClickListener.onCollectionClick(currentItem);
    }

    @Override
    public void bind(ShotsCollection item) {
        currentItem = item;
        headerTextView.setText(item.getName());
        smallHeaderTextView.setText(item.getName());
        shotCountTextView.setText(String.valueOf(item.shots().size()));
        adapter.setShotList(item.shots());
    }

    private void initializeRecyclerView(Context context) {
        adapter = new CollectionShotsAdapter(
                shot -> collectionClickListener.onShotClick(shot, currentItem));

        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                if (currentItem != null) {
                    onGetMoreCollectionShotsListener.onGetMoreCollectionShots(currentItem);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
