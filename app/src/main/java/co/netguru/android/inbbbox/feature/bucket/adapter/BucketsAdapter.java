package co.netguru.android.inbbbox.feature.bucket.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;

public final class BucketsAdapter extends RecyclerView.Adapter<BaseBucketViewHolder> {

    private static final int TYPE_LIST_BUCKET_SHOTS_VIEW_TYPE = 1;
    private static final int TYPE_GRID_BUCKET_SHOTS_VIEW_TYPE = 2;

    private final BaseBucketViewHolder.BucketClickListener bucketClickListener;

    private List<BucketWithShots> bucketWithShotsList;
    private boolean isGridMode;

    public BucketsAdapter(BaseBucketViewHolder.BucketClickListener bucketClickListener) {
        this.bucketClickListener = bucketClickListener;
        this.bucketWithShotsList = Collections.emptyList();
    }

    @Override
    public BaseBucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST_BUCKET_SHOTS_VIEW_TYPE:
                return new BucketShotsListViewHolder(parent, bucketClickListener);
            case TYPE_GRID_BUCKET_SHOTS_VIEW_TYPE:
                return new BucketShotsGridViewHolder(parent, bucketClickListener);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(BaseBucketViewHolder holder, int position) {
        BucketWithShots currentBucketWithShots = bucketWithShotsList.get(position);
        holder.bind(currentBucketWithShots);
    }

    @Override
    public int getItemCount() {
        return bucketWithShotsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isGridMode ? TYPE_GRID_BUCKET_SHOTS_VIEW_TYPE : TYPE_LIST_BUCKET_SHOTS_VIEW_TYPE;
    }

    public List<BucketWithShots> getData() {
        return bucketWithShotsList;
    }

    public void setNewBucketsWithShots(List<BucketWithShots> bucketWithShotsList) {
        this.bucketWithShotsList = bucketWithShotsList;
        notifyDataSetChanged();
    }

    public void addNewBucketsWithShots(Collection<BucketWithShots> bucketWithShotsList) {
        int oldSize = this.bucketWithShotsList.size();
        this.bucketWithShotsList.addAll(bucketWithShotsList);
        notifyItemRangeInserted(oldSize, bucketWithShotsList.size() + 1);
    }

    public void addNewBucketWithShots(BucketWithShots bucketWithShots) {
        bucketWithShotsList.add(0, bucketWithShots);
        notifyItemInserted(0);
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    public void removeBucketWithGivenIdIfExists(long bucketId) {
        for (int i = 0; i < bucketWithShotsList.size(); i++) {
            Bucket currentBucket = bucketWithShotsList.get(i).bucket();
            if (currentBucket.id() == bucketId) {
                bucketWithShotsList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }
}
