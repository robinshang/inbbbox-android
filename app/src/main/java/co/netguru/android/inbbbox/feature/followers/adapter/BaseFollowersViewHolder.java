package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.view.RoundedCornersFourImagesView;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseFollowersViewHolder extends BaseViewHolder<Follower> {

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

    BaseFollowersViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Follower item) {
        final float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        fourImagesView.setRadius(radius);
        userName.setText(item.name());
        shotsCount.setText(getShotCountString(item.shotsCount()));
        loadUserPhoto(item.avatarUrl());
    }

    protected void loadShotImages(String url1, String url2, String url3, String url4) {
        loadImageInto(fourImagesView.getTopLeftImageView(), url1);
        loadImageInto(fourImagesView.getTopRightImageView(), url2);
        loadImageInto(fourImagesView.getBottomLeftImageView(), url3);
        loadImageInto(fourImagesView.getBottomRightImageView(), url4);
    }

    private void loadImageInto(ImageView imageView, String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .placeholder(R.drawable.shot_placeholder)
                .animate(android.R.anim.fade_in)
                .into(imageView);
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
}
