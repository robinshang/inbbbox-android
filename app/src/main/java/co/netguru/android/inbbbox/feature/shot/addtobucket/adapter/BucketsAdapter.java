package co.netguru.android.inbbbox.feature.shot.addtobucket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;

public class BucketsAdapter extends RecyclerView.Adapter<BucketViewHolder> {

    private final BucketViewHolder.BucketClickListener bucketClickListener;
    private List<Bucket> buckets = new ArrayList<>();


    public BucketsAdapter(BucketViewHolder.BucketClickListener bucketClickListener) {
        this.bucketClickListener = bucketClickListener;
    }

    @Override
    public BucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BucketViewHolder(parent, bucketClickListener);
    }

    @Override
    public void onBindViewHolder(BucketViewHolder holder, int position) {
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

    public void addBucketOnTop(Bucket bucket) {
        buckets.add(0, bucket);
        notifyItemInserted(0);
    }
}