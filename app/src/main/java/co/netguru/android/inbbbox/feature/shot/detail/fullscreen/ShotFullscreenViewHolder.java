package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

class ShotFullscreenViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.shot_fullscreen_image)
    ImageView shotImageView;

    ShotFullscreenViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Shot shot) {
        setupImage(shot);
    }

    private void setupImage(Shot shot) {
        final Context context = itemView.getContext();
        ShotLoadingUtil.loadMainViewShotNoPlaceholder(context, shotImageView, shot);
    }
}
