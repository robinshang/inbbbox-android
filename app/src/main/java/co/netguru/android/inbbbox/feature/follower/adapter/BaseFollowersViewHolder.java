package co.netguru.android.inbbbox.feature.follower.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersFourImageView;
import de.hdodenhof.circleimageview.CircleImageView;

abstract class BaseFollowersViewHolder extends BaseViewHolder<UserWithShots> {

    @BindString(R.string.follower_item_shot)
    String shotCountString;
    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindView(R.id.four_image_view)
    RoundedCornersFourImageView fourImagesView;
    @BindView(R.id.follower_item_user_photo)
    CircleImageView userPhoto;
    @BindView(R.id.follower_item_username)
    TextView userName;
    @BindView(R.id.follower_item_shots_count)
    TextView shotsCount;

    private UserWithShots currentUserWithShots;

    BaseFollowersViewHolder(View view, @NonNull OnFollowerClickListener onFollowerClickListener) {
        super(view);
        view.setOnClickListener(v -> onFollowerClickListener.onClick(currentUserWithShots));
    }

    @Override
    public void bind(UserWithShots item) {
        this.currentUserWithShots = item;
        userName.setText(item.user().name());
        shotsCount.setText(getShotCountString(item.user().shotsCount()));
        loadUserPhoto(item.user().avatarUrl());
    }

    void loadShotImages(Shot leftTopShot, Shot rightTopShot, Shot leftBottomShot, Shot rightBottomShot) {
        loadImageInto(fourImagesView.getTopLeftImage(), leftTopShot);
        loadImageInto(fourImagesView.getTopRightImage(), rightTopShot);
        loadImageInto(fourImagesView.getBottomLeftImage(), leftBottomShot);
        loadImageInto(fourImagesView.getBottomRightImage(), rightBottomShot);
    }

    private void loadImageInto(ImageView imageView, Shot shot) {
        ShotLoadingUtil.loadListShot(itemView.getContext(), imageView, shot);
    }

    private String getShotCountString(int shotCount) {
        return String.valueOf(shotCount) + ' ' + shotCountString;
    }

    private void loadUserPhoto(String photoUrl) {
        Glide.clear(userPhoto);
        Glide.with(itemView.getContext())
                .load(photoUrl)
                .placeholder(R.drawable.logo_ball)
                .animate(android.R.anim.fade_in)
                .into(userPhoto);
    }
}
