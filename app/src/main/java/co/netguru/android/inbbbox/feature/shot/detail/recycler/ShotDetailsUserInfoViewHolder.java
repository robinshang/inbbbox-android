package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shot.detail.BucketedStatusChangeEmitter;
import co.netguru.android.inbbbox.feature.shot.detail.BucketedStatusChangeListener;

public class ShotDetailsUserInfoViewHolder extends ShotDetailsViewHolder<Shot>
        implements BucketedStatusChangeListener {

    @BindView(R.id.details_user_imageView)
    ImageView userAvatarImageView;

    @BindView(R.id.details_title_textView)
    TextView shotTitleTextView;

    @BindView(R.id.details_info_textView)
    TextView shotDateInfoTextView;

    @BindView(R.id.details_likes_count_textView)
    TextView likesCountTextView;

    @BindView(R.id.details_buckets_count_textView)
    TextView bucketCountTextView;

    @BindView(R.id.details_author_textView)
    TextView authorTextView;

    @BindView(R.id.details_team_textView)
    TextView teamTextView;

    @BindView(R.id.for_label_textView)
    TextView forLabel;

    @BindString(R.string.info_when_pattern)
    String infoWhenPattern;

    @BindString(R.string.info_where_pattern)
    String infoWherePattern;

    @BindView(R.id.details_likes_imageView)
    View likeImageView;

    @BindView(R.id.details_bucket_imageView)
    View bucketImageView;

    private static final String APP_NAME_KEY = "${where}";
    private static final String DATE_KEY = "${when}";
    private boolean isLiked;
    private boolean isBucketed;
    @Nullable
    private Shot item;

    ShotDetailsUserInfoViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback,
                                  BucketedStatusChangeEmitter bucketedStatusChangeEmitter) {
        super(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_info_user_info_layout, parent, false),
                actionCallback);
        bucketedStatusChangeEmitter.setListener(this);
    }

    @OnClick(R.id.details_likes_imageView)
    void onLikeShotClick() {
        isLiked = !isLiked;
        actionCallbackListener.onShotLikeAction(isLiked);
        updateActionsState();
    }

    @OnClick(R.id.details_bucket_imageView)
    void onBucketClick() {
        actionCallbackListener.onShotBucket(item.id());
        updateActionsState();
    }

    @OnClick(R.id.details_team_textView)
    void onCompanyClick() {
        actionCallbackListener.onTeamSelected(item.team());
    }

    @OnClick(R.id.details_author_textView)
    void onAuthorClick() {
        actionCallbackListener.onUserSelected(item.author());
    }

    @OnClick(R.id.details_user_imageView)
    void onUserImageClick() {
        actionCallbackListener.onUserSelected(item.author());
    }

    @Override
    public void bind(@NonNull Shot item) {
        this.item = item;
        shotTitleTextView.setText(item.title());
        showImage(item.author().avatarUrl());
        showAuthorInfo(item.author().name());
        showTeamInfo(item.team());
        showInfo(item.projectUrl(), DateTimeFormatUtil.getShotDetailsDate(item.creationDate()));
        showCounters(item.likesCount(), item.bucketCount());

        isLiked = item.isLiked();
        updateActionsState();
    }

    @Override
    public void onBucketedStatusChanged(boolean isInBucket) {
        this.isBucketed = isInBucket;
    }

    private void showTeamInfo(Team team) {
        if (team != null) {
            teamTextView.setText(team.name());
            teamTextView.setVisibility(View.VISIBLE);
            forLabel.setVisibility(View.VISIBLE);
        } else {
            teamTextView.setVisibility(View.GONE);
            forLabel.setVisibility(View.GONE);
        }
    }

    private void updateActionsState() {
        likeImageView.setActivated(isLiked);
        bucketImageView.setActivated(isBucketed);
    }

    private void showCounters(Integer likeCount, Integer bucketCount) {
        likesCountTextView.setText(likeCount.toString());
        bucketCountTextView.setText(bucketCount.toString());
    }

    private void showInfo(String projectName, String date) {
        String info = "";
        if (projectName != null && !projectName.isEmpty()) {
            infoWherePattern = infoWherePattern.replace(APP_NAME_KEY, projectName);
            info += infoWherePattern;
        }
        info += infoWhenPattern.replace(DATE_KEY, date);
        shotDateInfoTextView.setText(info);
    }

    private void showAuthorInfo(String author) {
        if (author != null && !author.isEmpty()) {
            authorTextView.setText(author);
        } else {
            authorTextView.setVisibility(View.GONE);
        }
    }

    private void showImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userAvatarImageView);
    }
}
