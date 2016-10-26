package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;

public class ShotsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_shot_image)
    ImageView shotImageView;

    private ImageLoader imageLoader;

    ShotsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }

    void bind(Shot shot) {
        setupImage(shot);
    }

    private void setupImage(Shot shot) {
        float radius = itemView.getContext().getResources().getDimension(R.dimen.shot_corner_radius);
        imageLoader.enableRoundCornersTransformationForNextRequest(radius);
        imageLoader.loadImageWithThumbnail(shotImageView, shot);
    }
}
