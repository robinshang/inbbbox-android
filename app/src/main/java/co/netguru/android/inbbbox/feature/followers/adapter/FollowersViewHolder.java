package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;

import co.netguru.android.inbbbox.feature.common.BaseViewHolder;

import co.netguru.android.inbbbox.view.RoundedCornersFourImagesView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersViewHolder extends BaseViewHolder<Follower> {

    @BindString(R.string.follower_item_shot)
    String shotCountString;

    @BindView(R.id.four_image_view)
    RoundedCornersFourImagesView fourImagesView;
    @BindView(R.id.follower_item_user_photo)
    CircleImageView userPhoto;
    @BindView(R.id.follower_item_username)
    TextView userName;
    @BindView(R.id.follower_item_shots_count)
    TextView shotsCount;

    FollowersViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Follower item) {
        final float radius = itemView.getResources().getDimension(R.dimen.like_corner_radius);
        userName.setText(item.name());
        shotsCount.setText(getShotCountString(item.shotsCount()));
        fourImagesView.setRadius(radius);
        loadUserPhoto(item.avatarUrl());

        Glide.with(itemView.getContext())
                .load(item.shotList().get(0).normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(fourImagesView.getTopLeftImageView());
        Glide.with(itemView.getContext())
                .load(item.shotList().get(1).normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(fourImagesView.getTopRightImageView());
        Glide.with(itemView.getContext())
                .load(item.shotList().get(2).normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(fourImagesView.getBottomLeftImageView());
        Glide.with(itemView.getContext())
                .load(item.shotList().get(3).normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(fourImagesView.getBottomRightImageView());
    }

    private String getShotCountString(int shotCount) {
        return String.valueOf(shotCount) + ' ' + shotCountString;
    }

    private void loadUserPhoto(String photoUrl) {
        Glide.with(itemView.getContext())
                .load(photoUrl)
                .placeholder(R.drawable.logo_ball)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .animate(android.R.anim.fade_in)
                .into(userPhoto);
    }
}
