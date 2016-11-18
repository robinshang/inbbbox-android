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
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

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

    @BindView(R.id.details_company_textView)
    TextView teamTextView;

    @BindString(R.string.info_pattern)
    String infoPattern;

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
        actionCallbackListener.onShotLikeAction(item.id(), isLiked);
        updateActionsState();
    }

    @OnClick(R.id.details_bucket_imageView)
    void onBucketClick() {
        isBucketed = !isBucketed;
        actionCallbackListener.onShotBucket(item.id(), isBucketed);
        updateActionsState();
    }

    @OnClick(R.id.details_company_textView)
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
        showInfo(item.projectUrl(), LocalTimeFormatter.getShotDetailsDate(item.date()));
        showCounters(item.likesCount(), item.bucketCount());

        isLiked = item.isLiked();
        isBucketed = item.isBucketed();
        updateActionsState();
    }

    private void showTeamInfo(Team team) {
        if(team != null){
            teamTextView.setText(team.name());
        }else{
            // TODO: 18.11.2016 hide team label
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

    private void showInfo(String appName, String date) {
        infoPattern = infoPattern.replace(APP_NAME_KEY, appName);
        infoPattern = infoPattern.replace(DATE_KEY, date);
        shotDateInfoTextView.setText(infoPattern);
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
