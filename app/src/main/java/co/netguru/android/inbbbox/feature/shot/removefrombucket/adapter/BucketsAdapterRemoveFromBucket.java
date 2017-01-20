package co.netguru.android.inbbbox.feature.shot.removefrombucket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;

public class BucketsAdapterRemoveFromBucket extends RecyclerView.Adapter<BucketViewHolderRemoveFromBucket> {

    private final BucketViewHolderRemoveFromBucket.CheckboxChangeListener checkboxChangeListener;
    private List<Bucket> buckets = new ArrayList<>();


    public BucketsAdapterRemoveFromBucket(BucketViewHolderRemoveFromBucket.CheckboxChangeListener checkboxChangeListener) {
        this.checkboxChangeListener = checkboxChangeListener;
    }

    @Override
    public BucketViewHolderRemoveFromBucket onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BucketViewHolderRemoveFromBucket(parent, checkboxChangeListener);
    }

    @Override
    public void onBindViewHolder(BucketViewHolderRemoveFromBucket holder, int position) {
        holder.bind(buckets.get(position));
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }

    public void setBuckets(List<Bucket> bucketsToDisplay) {
        buckets = bucketsToDisplay;
        notifyDataSetChanged();
    }

    public void addMoreBuckets(List<Bucket> bucketsToAdd) {
        int oldSize = this.buckets.size();
        this.buckets.addAll(bucketsToAdd);
        notifyItemRangeInserted(oldSize, bucketsToAdd.size() + 1);
    }
}