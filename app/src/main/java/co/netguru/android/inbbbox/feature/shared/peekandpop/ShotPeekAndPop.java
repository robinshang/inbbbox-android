package co.netguru.android.inbbbox.feature.shared.peekandpop;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class ShotPeekAndPop extends PeekAndPop implements ShotPeekAndPopContract.View, PeekAndPop.OnGeneralActionListener {

    private static final int VIBRATE_DURATION_MS = 30;
    private static final String APP_NAME_KEY = "${where}";
    private static final String DATE_KEY = "${when}";
    @BindView(R.id.rounded_corners_shot_image_view)
    RoundedCornersShotImageView shotImageView;
    @BindView(R.id.details_user_imageView)
    CircleImageView userImageView;
    @BindView(R.id.details_title_textView)
    TextView titleTextView;
    @BindView(R.id.details_author_textView)
    TextView authorTextView;
    @BindView(R.id.details_info_textView)
    TextView dateTextView;
    @BindView(R.id.for_label_textView)
    TextView forLabel;
    @BindView(R.id.details_likes_imageView)
    ImageView likeImageView;
    @BindView(R.id.details_bucket_imageView)
    ImageView bucketImageView;
    @BindView(R.id.details_team_textView)
    TextView teamTextView;
    @BindView(R.id.details_likes_count_textView)
    TextView likesCountTextView;
    @BindView(R.id.details_buckets_count_textView)
    TextView bucketsCountTextView;
    @BindString(R.string.info_when_pattern)
    String infoWhenPattern;
    @BindString(R.string.info_where_pattern)
    String infoWherePattern;
    private ShotPeekAndPopListener listener;

    private Shot shot;

    private ShotPeekAndPopPresenter presenter;

    private OnGeneralActionListener extraGeneralListener;

    public ShotPeekAndPop(Builder builder) {
        super(builder);
        initHoldAndReleaseViews();
    }

    public void setShotPeekAndPopListener(ShotPeekAndPopListener listener) {
        this.listener = listener;
    }

    private void initHoldAndReleaseViews() {
        addHoldAndReleaseView(R.id.details_likes_imageView);
        addHoldAndReleaseView(R.id.details_bucket_imageView);
        setOnHoldAndReleaseListener(getHoldAndReleaseListener());
        setOnGeneralActionListener(this);
    }

    @Override
    public void setOnGeneralActionListener(@Nullable OnGeneralActionListener onGeneralActionListener) {
        if(onGeneralActionListener == this) {
            super.setOnGeneralActionListener(onGeneralActionListener);
        } else {
            extraGeneralListener = onGeneralActionListener;
        }
    }

    private void initComponent() {
        presenter = App.getUserComponent(contentView.getContext())
                .plusPeekAndPopComponent().getPresenter();
    }

    public void bindPeekAndPop(Shot shot) {
        this.shot = shot;
        ButterKnife.bind(this, getPeekView());

        titleTextView.setText(shot.title());
        showImage(shot.author().avatarUrl());
        showAuthorInfo(shot.author().name());
        showTeamInfo(shot.team());
        showInfo(shot.projectUrl(), DateTimeFormatUtil.getShotDetailsDate(shot.creationDate()));
        showCounters(shot.likesCount(), shot.bucketCount());
        setIcons();
        ShotLoadingUtil.loadMainViewShot(getPeekView().getContext(),
                shotImageView.getImageView(), shot);
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

    private void showAuthorInfo(String author) {
        if (author != null && !author.isEmpty()) {
            authorTextView.setText(author);
        } else {
            authorTextView.setVisibility(View.GONE);
        }
    }

    private void showImage(String url) {
        Glide.with(getPeekView().getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImageView);
    }

    private void showInfo(String projectName, String date) {
        String info = "";
        if (projectName != null && !projectName.isEmpty()) {
            infoWherePattern = infoWherePattern.replace(APP_NAME_KEY, projectName);
            info += infoWherePattern;
        }
        info += infoWhenPattern.replace(DATE_KEY, date);
        dateTextView.setText(info);
    }

    private void showCounters(Integer likeCount, Integer bucketCount) {
        likesCountTextView.setText(likeCount.toString());
        bucketsCountTextView.setText(bucketCount.toString());
    }

    private void setIcons() {
        likeImageView.setActivated(shot.isLiked());
        bucketImageView.setActivated(shot.isBucketed());
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getPeekView().getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VIBRATE_DURATION_MS);
    }

    private PeekAndPop.OnHoldAndReleaseListener getHoldAndReleaseListener() {
        return new PeekAndPop.OnHoldAndReleaseListener() {
            @Override
            public void onHold(View view, int i) {
                view.setActivated(true);
                vibrate();
            }

            @Override
            public void onLeave(View view, int i) {
                view.setActivated(false);
            }

            @Override
            public void onRelease(View view, int i) {
                if (listener != null) {
                    switch (view.getId()) {
                        case R.id.details_likes_imageView:
                            presenter.toggleLikeShot(shot);
                            break;

                        case R.id.details_bucket_imageView:
                            listener.onShotBucketed(shot);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void showMessageOnServerError(String errorMessage) {
        Snackbar.make(contentView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPeek(View view, int i) {
        initComponent();

        if(extraGeneralListener != null) {
            extraGeneralListener.onPeek(view, i);
        }
    }

    @Override
    public void onPop(View view, int i) {
        presenter.detach();

        if(extraGeneralListener != null) {
            extraGeneralListener.onPop(view, i);
        }
    }

    public interface ShotPeekAndPopListener {
        void onShotBucketed(Shot shot);
    }

}
