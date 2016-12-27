package co.netguru.android.inbbbox.feature.bucket.detail.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;

public class BucketShotViewHolder extends BaseViewHolder<ShotEntity> {

    @BindView(R.id.bucket_shot_item_image_view)
    ImageView bucketShotImage;

    private ShotEntity currentShotEntity;

    public BucketShotViewHolder(ViewGroup parent, OnShotInBucketClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_shot_item, parent, false));
        itemView.setOnClickListener(v -> shotClickListener.onShotClick(currentShotEntity));
    }

    @Override
    public void bind(ShotEntity item) {
        currentShotEntity = item;
        ShotLoadingUtil.loadListShot(itemView.getContext(), bucketShotImage, currentShotEntity.image());
    }


    @FunctionalInterface
    public interface OnShotInBucketClickListener {
        void onShotClick(ShotEntity shotEntity);
    }
}
