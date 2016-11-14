package co.netguru.android.inbbbox.feature.followers.details.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class FollowerDetailsGridViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.follower_details_item_image_view)
    RoundedCornersImageView imageView;

    FollowerDetailsGridViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_detail_item_grid_view, parent, false));
    }

    @Override
    public void bind(Shot item) {
        final float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        imageView.setRadius(radius);
        Glide.with(itemView.getContext())
                .load(item.normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(imageView);
    }
}
