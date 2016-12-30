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
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersFourImagesView;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseFollowersViewHolder extends BaseViewHolder<User> {

    protected static final int FIRST_SHOT = 0;
    protected static final int SECOND_SHOT = 1;
    protected static final int THIRD_SHOT = 2;
    protected static final int FOURTH_SHOT = 3;

    @BindString(R.string.follower_item_shot)
    String shotCountString;
    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindView(R.id.four_image_view)
    RoundedCornersFourImagesView fourImagesView;
    @BindView(R.id.follower_item_user_photo)
    CircleImageView userPhoto;
    @BindView(R.id.follower_item_username)
    TextView userName;
    @BindView(R.id.follower_item_shots_count)
    TextView shotsCount;

    private User currentFollower;

    BaseFollowersViewHolder(View view, @NonNull OnFollowerClickListener onFollowerClickListener) {
        super(view);
        view.setOnClickListener(v -> onFollowerClickListener.onClick(currentFollower));
    }

    @Override
    public void bind(User item) {
        this.currentFollower = item;
        fourImagesView.setRadius(radius);
        userName.setText(item.name());
        shotsCount.setText(getShotCountString(item.shotsCount()));
        loadUserPhoto(item.avatarUrl());
    }

    protected void loadShotImages(Shot leftTopShot, Shot rightTopShot, Shot leftBottomShot, Shot rightBottomShot) {
        loadImageInto(fourImagesView.getTopLeftImageView(), leftTopShot);
        loadImageInto(fourImagesView.getTopRightImageView(), rightTopShot);
        loadImageInto(fourImagesView.getBottomLeftImageView(), leftBottomShot);
        loadImageInto(fourImagesView.getBottomRightImageView(), rightBottomShot);
    }

    private void loadImageInto(ImageView imageView, Shot shot) {
        ShotLoadingUtil.loadListShot(itemView.getContext(), imageView, shot);
    }

    private String getShotCountString(int shotCount) {
        return String.valueOf(shotCount) + ' ' + shotCountString;
    }

    private void loadUserPhoto(String photoUrl) {
        Glide.with(itemView.getContext())
                .load(photoUrl)
                .placeholder(R.drawable.logo_ball)
                .animate(android.R.anim.fade_in)
                .into(userPhoto);
    }

    @FunctionalInterface
    public interface OnFollowerClickListener {
        void onClick(User follower);
    }
}
