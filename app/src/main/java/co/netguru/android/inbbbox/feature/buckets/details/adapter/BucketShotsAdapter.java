package co.netguru.android.inbbbox.feature.buckets.details.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import co.netguru.android.inbbbox.model.api.ShotEntity;

public class BucketShotsAdapter extends RecyclerView.Adapter<BucketShotViewHolder> {

    private static final int TYPE_GRID_SHOT_VIEW_TYPE = 1;
    private static final int TYPE_LIST_SHOT_VIEW_TYPE = 2;
    private final List<ShotEntity> shotEntityList;
    private final BucketShotViewHolder.OnShotInBucketClickListener onShotInBucketClickListener;
    private boolean isGridMode;

    public BucketShotsAdapter(@NonNull BucketShotViewHolder.OnShotInBucketClickListener onShotInBucketClickListener) {
        this.onShotInBucketClickListener = onShotInBucketClickListener;
        shotEntityList = new ArrayList<>();
    }

    @Override
    public BucketShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BucketShotViewHolder(parent, onShotInBucketClickListener);
    }

    @Override
    public void onBindViewHolder(BucketShotViewHolder holder, int position) {
        holder.bind(shotEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotEntityList.size();
    }

    public void setNewShots(Collection<ShotEntity> shotsToSet) {
        shotEntityList.clear();
        shotEntityList.addAll(shotsToSet);
        notifyDataSetChanged();
    }

    public void addNewBucketWithShots(Collection<ShotEntity> shotsToAdd) {
        int oldSize = this.shotEntityList.size();
        this.shotEntityList.addAll(shotsToAdd);
        notifyItemRangeInserted(oldSize, shotsToAdd.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return isGridMode ? TYPE_GRID_SHOT_VIEW_TYPE : TYPE_LIST_SHOT_VIEW_TYPE;
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

}
