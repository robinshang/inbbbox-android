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

    @BindView(R.id.details_user_imageView)
    ImageView userAvatarImageView;

    @BindView(R.id.details_author_textView)
    TextView shotTitleTextView;

    @BindView(R.id.details_info_textView)
    TextView shotDateInfoTextView;

    @BindString(R.string.details_author_pattern)
    String authorPattern;

    @BindString(R.string.details_shot_info_pattern)
    String infoPattern;

    @OnClick(R.id.details_likes_imageView)
    void onLikeShotClick() {
        // TODO: 14.11.2016 handle like click
    }

    @OnClick(R.id.details_bucket_imageView)
    void onBucketClick() {
        // TODO: 14.11.2016 handle bucket click
    }

    ShotDetailsUserInfoViewHolder(View view) {
        super(view);
    }

    @Override
    protected void handleBinding() {
        showImage(item.userAvatarUrl());
        showAuthorInfo(item.authorName(), item.authorUrl());
        showCompanyInfo(item.companyName(), item.companyProfileUrl());
        showInfo(item.appName(), item.date());
        showCounters(item.likesCount(), item.bucketCount());
    }

    private void showCounters(Integer likeCount, Integer bucketCount) {
// TODO: 14.11.2016
    }

    private void showInfo(String appName, String date) {
        // TODO: 14.11.2016  
    }

    private void showCompanyInfo(String company, String url) {
        // TODO: 14.11.2016  
    }

    private void showAuthorInfo(String author, String url) {
        // TODO: 14.11.2016
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
