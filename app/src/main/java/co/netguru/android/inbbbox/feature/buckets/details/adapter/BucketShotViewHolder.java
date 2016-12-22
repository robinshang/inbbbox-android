package co.netguru.android.inbbbox.feature.buckets.details.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;

public class BucketShotViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.bucket_shot_item_image_view)
    ImageView bucketShotImage;

    private Shot currentShot;

    BucketShotViewHolder(ViewGroup parent, OnShotInBucketClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_shot_item, parent, false));
        itemView.setOnClickListener(v -> shotClickListener.onShotClick(currentShot));
    }

    @Override
    public void bind(Shot item) {
        currentShot = item;
        ShotLoadingManager.loadListShot(itemView.getContext(), bucketShotImage, currentShot);
    }


    @FunctionalInterface
    public interface OnShotInBucketClickListener {
        void onShotClick(Shot shot);
    }
}
