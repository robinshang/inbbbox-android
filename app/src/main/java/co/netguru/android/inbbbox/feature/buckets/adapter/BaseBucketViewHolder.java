package co.netguru.android.inbbbox.feature.buckets.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.view.RoundedCornersBucketFourImageView;

public abstract class BaseBucketViewHolder extends BaseViewHolder<BucketWithShots> {

    @BindView(R.id.four_images_view)
    RoundedCornersBucketFourImageView fourImagesView;
    @BindView(R.id.bucket_shots_count_text_view)
    TextView bucketShotsCountTextView;
    @BindView(R.id.bucket_name_text_view)
    TextView bucketNameTextView;
    @BindView(R.id.empty_view)
    ImageView emptyView;

    private BucketWithShots bucketWithShots;

    public BaseBucketViewHolder(View view, BucketClickListener bucketClickListener) {
        super(view);
        itemView.setOnClickListener(v -> bucketClickListener.onBucketClick(bucketWithShots));
    }

    @Override
    public void bind(BucketWithShots item) {
        this.bucketWithShots = item;
        List<ShotEntity> shots = bucketWithShots.shots();
        if (shots.isEmpty()) {
            showEmptyView();
        } else {
            handleNotEmptyShotsList(shots);
        }

        bucketNameTextView.setText(bucketWithShots.bucket().name());
        String shotsString = itemView.getResources().getQuantityString(R.plurals.fragment_buckets_shots_string, shots.size());
        bucketShotsCountTextView.setText(String.format(shotsString, shots.size()));
    }

    private void loadImageInto(ImageView imageView, String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .animate(android.R.anim.fade_in)
                .into(imageView);
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        fourImagesView.setVisibility(View.GONE);
    }

    private void handleNotEmptyShotsList(List<ShotEntity> shots) {
        emptyView.setVisibility(View.GONE);
        fourImagesView.setVisibility(View.VISIBLE);
        if (shots.size() < 4) {
            showOnlyOneImage(shots.get(0).image().normalUrl());
        } else {
            showFourFirstImages(shots);
        }
    }

    private void showOnlyOneImage(String url) {
        fourImagesView.showBottomImages(false);
        loadImageInto(fourImagesView.getTopImageView(), url);
    }

    private void showFourFirstImages(List<ShotEntity> shots) {
        fourImagesView.showBottomImages(true);
        loadImageInto(fourImagesView.getTopImageView(), shots.get(0).image().normalUrl());
        loadImageInto(fourImagesView.getBottomFirstImage(), shots.get(1).image().normalUrl());
        loadImageInto(fourImagesView.getBottomSecondImage(), shots.get(2).image().normalUrl());
        loadImageInto(fourImagesView.getBottomThirdImage(), shots.get(3).image().normalUrl());
    }

    @FunctionalInterface
    public interface BucketClickListener {
        void onBucketClick(BucketWithShots bucketWithShots);
    }
}