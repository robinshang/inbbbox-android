package co.netguru.android.inbbbox.feature.like.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersImageView;

class LikesGridViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.like_item_image_view)
    RoundedCornersImageView imageView;

    @BindView(R.id.gif_label_textView)
    View gifLabelView;

    private final ShotClickListener likeClickListener;
    private Shot item;

    LikesGridViewHolder(ViewGroup parent, ShotClickListener likeClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item_list_view, parent, false));
        this.likeClickListener = likeClickListener;
    }

    @OnClick(R.id.like_item_image_view)
    void onItemClick() {
        likeClickListener.onShotClick(item);
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
        ShotLoadingUtil.loadListShot(itemView.getContext(), imageView, item);
    }
}
