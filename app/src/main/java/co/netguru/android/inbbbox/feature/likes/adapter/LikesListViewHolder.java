package co.netguru.android.inbbbox.feature.likes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;

import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class LikesListViewHolder extends BaseViewHolder<LikedShot> {

    @BindView(R.id.like_item_image_view)
    RoundedCornersImageView imageView;

    LikesListViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item_list_view, parent, false));
    }

    @Override
    public void bind(LikedShot item) {
        final float radius = itemView.getResources().getDimension(R.dimen.like_corner_radius);
        imageView.setRadius(radius);
        Glide.with(itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(imageView);
    }
}