package co.netguru.android.inbbbox.feature.followers.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersFourImagesView;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseFollowersViewHolder extends BaseViewHolder<Follower> {

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

    private Follower currentFollower;

    BaseFollowersViewHolder(View view, @NonNull OnFollowerClickListener onFollowerClickListener) {
        super(view);
        view.setOnClickListener(v -> onFollowerClickListener.onClick(currentFollower));
    }

    @Override
    public void bind(Follower item) {
        this.currentFollower = item;
        fourImagesView.setRadius(radius);
        userName.setText(item.name());
        shotsCount.setText(getShotCountString(item.shotsCount()));
        loadUserPhoto(item.avatarUrl());
    }

    protected void loadShotImages(Shot shot1, Shot shot2, Shot shot3, Shot shot4) {
        loadImageInto(fourImagesView.getTopLeftImageView(), shot1);
        loadImageInto(fourImagesView.getTopRightImageView(), shot2);
        loadImageInto(fourImagesView.getBottomLeftImageView(), shot3);
        loadImageInto(fourImagesView.getBottomRightImageView(), shot4);
    }

    private void loadImageInto(ImageView imageView, Shot shot) {
        ShotLoadingManager.loadListShot(itemView.getContext(), imageView, shot);
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

    public interface OnFollowerClickListener {
        void onClick(Follower follower);
    }
}
