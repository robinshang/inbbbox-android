package co.netguru.android.inbbbox.bucket.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;

public final class BucketShotsGridViewHolder extends BaseBucketViewHolder {
    
    public BucketShotsGridViewHolder(ViewGroup parent, BucketClickListener bucketClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_with_shots_grid_item, parent, false), bucketClickListener);
    }
}
