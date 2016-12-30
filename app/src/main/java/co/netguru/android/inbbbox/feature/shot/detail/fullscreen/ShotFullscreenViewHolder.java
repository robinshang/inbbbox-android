package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import uk.co.senab.photoview.PhotoViewAttacher;

class ShotFullscreenViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.shot_fullscreen_image)
    ImageView shotImageView;

    private PhotoViewAttacher photoViewAttacher;

    ShotFullscreenViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Shot shot) {
        setupImage(shot);
    }

    private void setupImage(Shot shot) {
        final Context context = itemView.getContext();

        if (photoViewAttacher != null) {
            photoViewAttacher.update();
            photoViewAttacher.setScale(1);
        }

        ShotLoadingUtil.loadMainViewShotWithListenerNoPlaceholder(context, shotImageView, shot, new RequestListener() {

            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                if (photoViewAttacher == null) {
                    photoViewAttacher = new PhotoViewAttacher(shotImageView);
                }
                return false;
            }
        });
    }
}