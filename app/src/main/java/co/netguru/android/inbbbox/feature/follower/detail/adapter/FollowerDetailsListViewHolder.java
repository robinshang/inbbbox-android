package co.netguru.android.inbbbox.feature.follower.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersImageView;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;

public class FollowerDetailsListViewHolder extends BaseViewHolder<Shot> {

    private final ShotClickListener shotClickListener;

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindView(R.id.follower_details_item_image_view)
    RoundedCornersImageView imageView;

    @BindView(R.id.gif_label_textView)
    View gifLabelView;
    private Shot item;

    FollowerDetailsListViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follower_detail_item_list_view, parent, false));
        this.shotClickListener = shotClickListener;
    }

    @OnClick(R.id.follower_details_item_image_view)
    void onShotImageClick() {
        shotClickListener.onShotClick(item);
    }

    @Override
    public void bind(Shot item) {
        this.item = item;
        imageView.setRadius(radius);
        if (item.isGif()) {
            gifLabelView.setVisibility(View.VISIBLE);
        } else {
            gifLabelView.setVisibility(View.GONE);
        }
        ShotLoadingUtil.loadListShot(itemView.getContext(), imageView, item);
    }
}
