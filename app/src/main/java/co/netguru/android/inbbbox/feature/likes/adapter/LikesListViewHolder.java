package co.netguru.android.inbbbox.feature.likes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class LikesListViewHolder extends BaseViewHolder<LikedShot> {

    @BindView(R.id.like_item_image_view)
    RoundedCornersImageView imageView;

    @BindView(R.id.gif_label_textView)
    View gifLabelView;

    LikesListViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item_list_view, parent, false));
    }

    @Override
    public void bind(LikedShot item) {
        final float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        imageView.setRadius(radius);
        if (item.isGif()) {
            gifLabelView.setVisibility(View.VISIBLE);
        } else {
            gifLabelView.setVisibility(View.GONE);
        }
        ShotLoadingManager.loadListShot(itemView.getContext(), imageView, item);
    }
}