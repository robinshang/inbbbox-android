package co.netguru.android.inbbbox.feature.shared.peekandpop;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShotPeekAndPop extends PeekAndPop implements ShotPeekAndPopContract.View,
        PeekAndPop.OnGeneralActionListener {

    private final Set<OnGeneralActionListener> extraGeneralListeners = new HashSet<>();
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
    @BindString(R.string.info_when_format)
    String infoWhenFormat;
    @BindString(R.string.info_where_format)
    String infoWhereFormat;
    private ShotPeekAndPopPresenter presenter;
    private Fragment fragmentForBucketChooser;

    public ShotPeekAndPop(Builder builder) {
        super(builder);
        initHoldAndReleaseViews();
    }

    public static ShotPeekAndPop init(Activity activity, RecyclerView recyclerView,
                                      OnGeneralActionListener onGeneralActionListener,
                                      Fragment fragmentForBucketChooser) {
        ShotPeekAndPop peekAndPop = new ShotPeekAndPop(
                new PeekAndPop.Builder(activity)
                        .blurBackground(true)
                        .peekLayout(R.layout.peek_shot_details)
                        .parentViewGroupToDisallowTouchEvents(recyclerView));

        peekAndPop.setFragmentForBucketChooser(fragmentForBucketChooser);
        peekAndPop.addOnGeneralActionListener(onGeneralActionListener);

        return peekAndPop;
    }

    private void initHoldAndReleaseViews() {
        addHoldAndReleaseView(R.id.details_likes_imageView);
        addHoldAndReleaseView(R.id.details_bucket_imageView);
        setOnHoldAndReleaseListener(getHoldAndReleaseListener());
        setOnGeneralActionListener(this);
    }

    void setFragmentForBucketChooser(Fragment fragmentForBucketChooser) {
        if (fragmentForBucketChooser instanceof AddToBucketDialogFragment.BucketSelectListener) {
            this.fragmentForBucketChooser = fragmentForBucketChooser;
        } else {
            throw new IllegalArgumentException("Fragment must implement BucketSelectListener");
        }
    }

    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        presenter.addShotToBucket(shot, bucket);
    }

    public void addOnGeneralActionListener(OnGeneralActionListener onGeneralActionListener) {
        extraGeneralListeners.add(onGeneralActionListener);
    }

    public void removeOnGeneralActionListener(OnGeneralActionListener onGeneralActionListener) {
        extraGeneralListeners.remove(onGeneralActionListener);
    }

    @Override
    public void setOnGeneralActionListener(@Nullable OnGeneralActionListener onGeneralActionListener) {
        if (onGeneralActionListener == this) {
            super.setOnGeneralActionListener(onGeneralActionListener);
        } else {
            throw new RuntimeException("Call addOnGeneralActionListener instead");
        }
    }

    public void bindPeekAndPop(Shot shot) {
        ButterKnife.bind(this, getPeekView());

        titleTextView.setText(shot.title());
        showImage(shot.author().avatarUrl());
        showAuthorInfo(shot.author().name());
        showTeamInfo(shot.team());
        showInfo(shot.projectUrl(), DateTimeFormatUtil.getShotDetailsDate(shot.creationDate()));
        showCounters(shot.likesCount(), shot.bucketCount());
        setIcons(shot);
        ShotLoadingUtil.loadMainViewShot(getPeekView().getContext(),
                shotImageView.getImageView(), shot);

        initComponent(shot);
    }

    @Override
    public void showMessageOnServerError(String errorMessage) {
        Snackbar.make(contentView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessageShotLiked() {
        Snackbar.make(fragmentForBucketChooser.getView(),
                R.string.shot_liked, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessageShotUnliked() {
        Snackbar.make(fragmentForBucketChooser.getView(),
                R.string.shot_unliked, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void showBucketChooserView(Shot shot) {
        AddToBucketDialogFragment.newInstance(fragmentForBucketChooser, shot)
                .show(fragmentForBucketChooser.getActivity().getSupportFragmentManager(),
                        AddToBucketDialogFragment.TAG);
    }

    @Override
    public void onPeek(View view, int i) {
        for (OnGeneralActionListener listener : extraGeneralListeners) {
            listener.onPeek(view, i);
        }
    }

    @Override
    public void onPop(View view, int i) {
        if (presenter != null) {
            presenter.detach();
        }

        for (OnGeneralActionListener listener : extraGeneralListeners) {
            listener.onPop(view, i);
        }
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        if (fragmentForBucketChooser.getView() != null) {
            Snackbar.make(fragmentForBucketChooser.getView(), stringRes, Snackbar.LENGTH_LONG).show();
        }
    }

    private void initComponent(Shot shot) {
        presenter = App.getUserComponent(contentView.getContext())
                .plusPeekAndPopComponent(new ShotPeekAndPopModule(shot)).getPresenter();
        presenter.attachView(this);
    }

    private void showTeamInfo(Team team) {
        if (team != null) {
            teamTextView.setText(team.name());
            setLabelsVisibility(View.VISIBLE);
        } else {
            setLabelsVisibility(View.GONE);
        }
    }

    private void setLabelsVisibility(int visibility) {
        teamTextView.setVisibility(visibility);
        forLabel.setVisibility(visibility);
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
            info += String.format(infoWhereFormat, projectName);
        }
        info += String.format(infoWhenFormat, date);

        dateTextView.setText(info);
    }

    private void showCounters(Integer likeCount, Integer bucketCount) {
        likesCountTextView.setText(likeCount.toString());
        bucketsCountTextView.setText(bucketCount.toString());
    }

    private void setIcons(Shot shot) {
        likeImageView.setActivated(shot.isLiked());
        bucketImageView.setActivated(shot.isBucketed());
    }

    private PeekAndPop.OnHoldAndReleaseListener getHoldAndReleaseListener() {
        return new PeekAndPop.OnHoldAndReleaseListener() {
            @Override
            public void onHold(View view, int positionInAdapter) {
                view.setActivated(true);
                presenter.vibrate();
            }

            @Override
            public void onLeave(View view, int positionInAdapter) {
                view.setActivated(false);
            }

            @Override
            public void onRelease(View view, int positionInAdapter) {
                onFingerReleaseWhilePeeking(view);
            }
        };
    }

    private void onFingerReleaseWhilePeeking(View onView) {
        if (onView.getId() == R.id.details_likes_imageView) {
            presenter.toggleLikeShot();
        } else if (onView.getId() == R.id.details_bucket_imageView) {
            presenter.onBucketShot();
        }
    }

}
