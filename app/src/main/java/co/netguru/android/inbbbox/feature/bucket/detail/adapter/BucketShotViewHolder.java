package co.netguru.android.inbbbox.feature.bucket.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public class BucketShotViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.bucket_shot_item_view)
    RoundedCornersShotImageView shotImageView;

    private Shot currentShot;

    BucketShotViewHolder(ViewGroup parent, OnShotInBucketClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_shot_item, parent, false));
        itemView.setOnClickListener(v -> shotClickListener.onShotClick(currentShot));
    }

    @Override
    public void bind(Shot item) {
        currentShot = item;
        shotImageView.loadShot(item);
    }

    @FunctionalInterface
    public interface OnShotInBucketClickListener {
        void onShotClick(Shot shot);
    }
}
