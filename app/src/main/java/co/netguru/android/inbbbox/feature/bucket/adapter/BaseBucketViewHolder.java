package co.netguru.android.inbbbox.feature.bucket.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersBucketFourImageView;


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

    BaseBucketViewHolder(View view, BucketClickListener bucketClickListener) {
        super(view);
        itemView.setOnClickListener(v -> bucketClickListener.onBucketClick(bucketWithShots));
    }

    @Override
    public void bind(BucketWithShots item) {
        this.bucketWithShots = item;
        List<Shot> shots = bucketWithShots.shots();
        if (shots.isEmpty()) {
            showEmptyView();
        } else {
            handleNotEmptyShotsList(shots);
        }
        bucketNameTextView.setText(bucketWithShots.bucket().name());
        int shotsCount = item.bucket().shotsCount();
        String shotsString = itemView.getResources().getQuantityString(R.plurals.fragment_buckets_shots_string, shotsCount);
        bucketShotsCountTextView.setText(String.format(shotsString, shotsCount));
    }

    private void loadImageInto(ImageView imageView, String url) {
        Glide.clear(imageView);
        Glide.with(itemView.getContext())
                .load(url)
                .animate(android.R.anim.fade_in)
                .into(imageView);
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        fourImagesView.setVisibility(View.GONE);
    }

    private void handleNotEmptyShotsList(List<Shot> shots) {
        emptyView.setVisibility(View.GONE);
        fourImagesView.setVisibility(View.VISIBLE);
        if (shots.size() < 4) {
            showOnlyOneImage(shots.get(0).normalImageUrl());
        } else {
            showFourFirstImages(shots);
        }
    }

    private void showOnlyOneImage(String url) {
        fourImagesView.showBottomImages(false);
        loadImageInto(fourImagesView.getTopImageView(), url);
    }

    private void showFourFirstImages(List<Shot> shots) {
        fourImagesView.showBottomImages(true);
        loadImageInto(fourImagesView.getTopImageView(), shots.get(0).normalImageUrl());
        loadImageInto(fourImagesView.getBottomFirstImage(), shots.get(1).normalImageUrl());
        loadImageInto(fourImagesView.getBottomSecondImage(), shots.get(2).normalImageUrl());
        loadImageInto(fourImagesView.getBottomThirdImage(), shots.get(3).normalImageUrl());
    }

    @FunctionalInterface
    public interface BucketClickListener {
        void onBucketClick(BucketWithShots bucketWithShots);
    }
}