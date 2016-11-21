package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.utils.DateTimeFormatUtil;

class ShotDetailsUserInfoViewHolder extends ShotDetailsViewHolder {

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

    ShotDetailsUserInfoViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @OnClick(R.id.details_likes_imageView)
    void onLikeShotClick() {
        isLiked = !isLiked;
        actionCallbackListener.onShotLikeAction( isLiked);
        updateActionsState();
    }

    @OnClick(R.id.details_bucket_imageView)
    void onBucketClick() {
        isBucketed = !isBucketed;
        actionCallbackListener.onShotBucket(item.id(), isBucketed);
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

    @Override
    protected void handleBinding() {
        shotTitleTextView.setText(item.title());
        showImage(item.author().avatarUrl());
        showAuthorInfo(item.author().name());
        showTeamInfo(item.team());
        showInfo(item.projectUrl(), DateTimeFormatUtil.getShotDetailsDate(item.creationDate()));
        showCounters(item.likesCount(), item.bucketCount());

        isLiked = item.isLiked();
        isBucketed = item.isBucketed();
        updateActionsState();
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
        authorTextView.setText(author);
    }

    private void showImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userAvatarImageView);
    }
}
