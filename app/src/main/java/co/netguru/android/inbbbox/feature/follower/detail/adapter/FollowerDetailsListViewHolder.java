package co.netguru.android.inbbbox.feature.follower.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public class FollowerDetailsListViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.follower_details_item_image_view)
    RoundedCornersShotImageView imageView;

    private final ShotClickListener shotClickListener;

    private Shot item;

    FollowerDetailsListViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follower_detail_item_view, parent, false));
        this.shotClickListener = shotClickListener;
    }

    @OnClick(R.id.follower_details_item_image_view)
    void onShotImageClick() {
        shotClickListener.onShotClick(item);
    }

    @Override
    public void bind(Shot item) {
        this.item = item;
        imageView.loadShot(item);
    }
}
