package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;

public class ShotDetailsUserInfoViewHolder extends ShotDetailsViewHolder {

    private static final String APP_NAME_KEY = "${where}";
    private static final String DATE_KEY = "${when}";

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
    TextView companyTextView;

    @BindString(R.string.info_pattern)
    String infoPattern;

    @OnClick(R.id.details_likes_imageView)
    void onLikeShotClick() {
        // TODO: 14.11.2016 handle like click
    }

    @OnClick(R.id.details_bucket_imageView)
    void onBucketClick() {
        // TODO: 14.11.2016 handle bucket click
    }

    @OnClick(R.id.details_company_textView)
    void onCompanyyClick() {
        // TODO: 14.11.2016 pass url to company profile
    }

    @OnClick(R.id.details_author_textView)
    void onAuthrClick() {
        // TODO: 14.11.2016 open author profile
    }

    ShotDetailsUserInfoViewHolder(View view) {
        super(view);
    }

    @Override
    protected void handleBinding() {
        shotTitleTextView.setText(item.title());
        showImage(item.userAvatarUrl());
        showAuthorInfo(item.authorName(), item.companyName());
        showInfo(item.appName(), item.date());
        showCounters(item.likesCount(), item.bucketCount());
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


    private void showAuthorInfo(String author, String company) {
        authorTextView.setText(author);
        companyTextView.setText(company);
    }

    private void showImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_ball)
                .fitCenter()
                .into(userAvatarImageView);

    }
}
