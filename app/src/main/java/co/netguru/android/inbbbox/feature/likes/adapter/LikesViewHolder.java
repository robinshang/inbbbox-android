package co.netguru.android.inbbbox.feature.likes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;


public class LikesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.like_item_image_view)
    ImageView imageView;

    private final ImageLoader imageLoader;

    LikesViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.imageLoader = imageLoader;
    }

    void bind(LikedShot item) {
        imageLoader.loadImageWithRoundedCorners(imageView, null, item);
    }
}
