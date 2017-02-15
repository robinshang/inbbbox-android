package co.netguru.android.inbbbox.feature.shot.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import retrofit2.http.HEAD;

import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MIN;

class ShotsViewHolder extends BaseShotsViewHolder<Shot> implements DetailsVisibilityChangeListener {

    private final ShotSwipeListener shotSwipeListener;
    @BindView(R.id.user_name_textView)
    TextView userNameTextView;
    @BindView(R.id.comments_count_textView)
    TextView commentsCountTextView;
    @BindView(R.id.likes_count_textView)
    TextView likesCountTextView;
    @BindView(R.id.user_imageView)
    ImageView userImageView;
    @BindView(R.id.shot_details_layout)
    View shotDetailsView;
    @BindDrawable(R.drawable.shot_placeholder)
    Drawable shotPlaceHolder;
    private Shot shot;

    ShotsViewHolder(View itemView, @NonNull ShotSwipeListener shotSwipeListener,
                    @NonNull DetailsVisibilityChangeEmitter emitter) {
        super(itemView);
        this.shotSwipeListener = shotSwipeListener;
        emitter.setListener(this);
    }

    @OnClick(R.id.iv_shot_image)
    void onShotClick() {
        shotSwipeListener.onShotSelected(shot);
    }

    @Override
    public void bind(Shot shot) {
        this.shot = shot;
        setupImage(shot);
        setupDetails(shot);
        loadUserImage(shot.author().avatarUrl());
    }

    @Override
    public void onLeftSwipe() {
        shotSwipeListener.onShotLikeSwipe(shot);
    }

    @Override
    public void onLeftLongSwipe() {
        shotSwipeListener.onAddShotToBucketSwipe(shot);
    }

    @Override
    public void onRightSwipe() {
        shotSwipeListener.onCommentShotSwipe(shot);
    }

    @Override
    public void onRightLongSwipe() {
        shotSwipeListener.onFollowUserSwipe(shot);
    }

    @Override
    public void onStartSwipe() {
        shotSwipeListener.onStartSwipe(shot);
    }

    @Override
    public void onEndSwipe() {
        shotSwipeListener.onEndSwipe(shot);
    }

    @Override
    public void onDetailsChangeVisibility(boolean shouldBeVisible) {
        if (shouldBeVisible)
            shotDetailsView.setVisibility(View.VISIBLE);
        else
            shotDetailsView.setVisibility(View.INVISIBLE);
    }

    private void setupImage(Shot shot) {
        shotImageView.loadShot(shot);
        likeIcon.setActivated(shot.isLiked());
    }

    private void setupDetails(Shot shot) {
        if (shot.author() != null && shot.author().username() != null)
            userNameTextView.setText(shot.author().username());

        likesCountTextView.setText(String.valueOf(shot.likesCount()));
        commentsCountTextView.setText(String.valueOf(shot.commentsCount()));
    }

    private void loadUserImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImageView);
    }
}
