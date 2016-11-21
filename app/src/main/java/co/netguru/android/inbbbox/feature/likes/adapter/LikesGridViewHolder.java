package co.netguru.android.inbbbox.feature.likes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class LikesGridViewHolder extends BaseViewHolder<Shot> {

    private final LikeClickListener likeClickListener;
    @BindView(R.id.like_item_image_view)
    RoundedCornersImageView imageView;

    @BindView(R.id.gif_label_textView)
    View gifLabelView;
    private Shot item;

    LikesGridViewHolder(ViewGroup parent, LikeClickListener likeClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item_list_view, parent, false));
        this.likeClickListener = likeClickListener;
    }

    @OnClick(R.id.like_item_image_view)
    void onItemClick() {
        likeClickListener.onLikeShotClick(item);
    }

    @Override
    public void bind(Shot item) {
        this.item = item;
        final float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        if (item.isGif()) {
            gifLabelView.setVisibility(View.VISIBLE);
        } else {
            gifLabelView.setVisibility(View.GONE);
        }
        imageView.setRadius(radius);
        ShotLoadingManager.loadListShot(itemView.getContext(), imageView, item);
    }
}
