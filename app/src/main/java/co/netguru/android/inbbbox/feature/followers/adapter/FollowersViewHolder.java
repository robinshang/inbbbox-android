package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Follower;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;

import co.netguru.android.inbbbox.view.RoundedCornersImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersViewHolder extends BaseViewHolder<Follower> {

    @BindString(R.string.follower_item_shot)
    String shotCountString;

    @BindView(R.id.follower_item_user_shot)
    RoundedCornersImageView userShot;
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
        userName.setText(item.name());
        shotsCount.setText(getShotCountString(item.shotsCount()));
        loadUserPhoto(item.avatarUrl());
        loadUserShot();
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

    private void loadUserShot() {
        final float radius = itemView.getResources().getDimension(R.dimen.shot_radius_dp);
        userShot.setRadius(radius);
        Glide.with(itemView.getContext())
                .load("") // TODO: 07.11.2016 Change to user shot
                .placeholder(R.drawable.shot_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(userShot);
    }
}
