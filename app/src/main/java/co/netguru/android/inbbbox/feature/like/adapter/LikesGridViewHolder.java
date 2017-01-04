package co.netguru.android.inbbbox.feature.like.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

class LikesGridViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.like_item_image_view)
    RoundedCornersShotImageView imageView;

    private final ShotClickListener likeClickListener;
    private Shot item;

    LikesGridViewHolder(ViewGroup parent, ShotClickListener likeClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false));
        this.likeClickListener = likeClickListener;
    }

    @OnClick(R.id.like_item_image_view)
    void onItemClick() {
        likeClickListener.onShotClick(item);
    }

    @Override
    public void bind(Shot item) {
        this.item = item;
        imageView.loadShot(item);
    }
}
