package co.netguru.android.inbbbox.feature.followers.details.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindDimen;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class FollowerDetailsGridViewHolder extends BaseViewHolder<Shot> {

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindView(R.id.follower_details_item_image_view)
    RoundedCornersImageView imageView;

    @BindView(R.id.gif_label_textView)
    View gifLabelView;

    FollowerDetailsGridViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_detail_item_grid_view, parent, false));
    }

    @Override
    public void bind(Shot item) {
        imageView.setRadius(radius);
        if (item.isGif()) {
            gifLabelView.setVisibility(View.VISIBLE);
        } else {
            gifLabelView.setVisibility(View.GONE);
        }
        ShotLoadingManager.loadListShot(itemView.getContext(), imageView, item);
    }
}