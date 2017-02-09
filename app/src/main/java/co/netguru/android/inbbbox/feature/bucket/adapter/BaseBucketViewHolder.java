package co.netguru.android.inbbbox.feature.bucket.adapter;


import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.BucketImageView;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public abstract class BaseBucketViewHolder extends BaseViewHolder<BucketWithShots> implements RequestListener<String, GlideDrawable> {

    @BindView(R.id.four_images_view)
    BucketImageView bucketImageView;
    @BindView(R.id.one_image_view)
    RoundedCornersShotImageView bucketOneImageView;
    @BindView(R.id.bucket_shots_count_text_view)
    TextView bucketShotsCountTextView;
    @BindView(R.id.bucket_name_text_view)
    TextView bucketNameTextView;
    @BindView(R.id.empty_view)
    ImageView emptyView;

    private BucketWithShots bucketWithShots;
    private int resourcesReady = 0;

    BaseBucketViewHolder(View view, BucketClickListener bucketClickListener) {
        super(view);
        itemView.setOnClickListener(v -> bucketClickListener.onBucketClick(bucketWithShots));
    }

    @Override
    public void bind(BucketWithShots item) {
        resourcesReady = 0;
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

    public void onPause() {
        bucketImageView.onPause();
    }

    public void onResume() {
        bucketImageView.onResume();
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        bucketImageView.setVisibility(View.GONE);
        bucketOneImageView.setVisibility(View.GONE);
    }

    private void handleNotEmptyShotsList(List<Shot> shots) {
        emptyView.setVisibility(View.GONE);
        if (shots.size() < 4) {
            showOnlyOneImage(shots.get(0));
        } else {
            showFourFirstImages(shots);
        }
    }

    private void showOnlyOneImage(Shot shot) {
        bucketImageView.setVisibility(View.GONE);
        bucketOneImageView.setVisibility(View.VISIBLE);
        bucketOneImageView.loadShot(shot);
    }

    private void showFourFirstImages(List<Shot> shots) {
        bucketOneImageView.setVisibility(View.GONE);
        bucketImageView.setVisibility(View.VISIBLE);
        loadCroppedImageInto(bucketImageView.getImageView(0), shots.get(0).normalImageUrl());
        loadCroppedImageInto(bucketImageView.getImageView(1), shots.get(1).normalImageUrl());
        loadCroppedImageInto(bucketImageView.getImageView(2), shots.get(2).normalImageUrl());
        loadCroppedImageInto(bucketImageView.getImageView(3), shots.get(3).normalImageUrl());
    }

    private void loadCroppedImageInto(ImageView imageView, String url) {
        imageView.post(() -> {
            if (imageView.getWidth() != 0) {
                requestImage(imageView, url);
            } else {
                imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        requestImage(imageView, url);
                    }
                });
            }
        });
    }

    private void requestImage(ImageView imageView, String url) {
        Glide.clear(imageView);
        Glide.with(itemView.getContext())
                .load(url)
                .centerCrop()
                .listener(BaseBucketViewHolder.this)
                .override(imageView.getWidth(), (int) (imageView.getHeight() * BucketImageView.BIG_IMAGE_HEIGHT_FACTOR))
                .into(imageView);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        resourcesReady++;

        if (resourcesReady == 4) {
            bucketImageView.startAnimation();
        }
        return false;
    }

    @FunctionalInterface
    public interface BucketClickListener {
        void onBucketClick(BucketWithShots bucketWithShots);
    }
}
