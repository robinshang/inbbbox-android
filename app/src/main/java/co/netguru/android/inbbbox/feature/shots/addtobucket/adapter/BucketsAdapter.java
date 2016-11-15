package co.netguru.android.inbbbox.feature.shots.addtobucket.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;

public class BucketsAdapter extends RecyclerView.Adapter<BucketViewHolder> {

    private final BucketViewHolder.BucketClickListener bucketClickListener;
    private List<Bucket> buckets = Collections.emptyList();

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

    public void showBucketsList(List<Bucket> bucketsToDisplay) {
        buckets = bucketsToDisplay;
        notifyDataSetChanged();
    }
}