package co.netguru.android.inbbbox.feature.shots.addtobucket;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.threeten.bp.LocalDateTime;

import java.util.Collections;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpDialogFragment;
import co.netguru.android.inbbbox.feature.details.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.details.ShotDetailsType;
import co.netguru.android.inbbbox.feature.details.fullscreen.ShotFullscreenActivity;
import co.netguru.android.inbbbox.feature.shots.addtobucket.adapter.BucketViewHolder;
import co.netguru.android.inbbbox.feature.shots.addtobucket.adapter.BucketsAdapter;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.view.DividerItemDecorator;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddToBucketDialogFragment extends BaseMvpDialogFragment<AddToBucketContract.View, AddToBucketContract.Presenter>
        implements AddToBucketContract.View, BucketViewHolder.BucketClickListener {

    public static final String TAG = AddToBucketDialogFragment.class.getSimpleName();

    private static final String SHOT_ARG_KEY = "shot_arg_key";
    private static final int FRAGMENT_REQUEST_CODE = 1;

    @BindView(R.id.shot_preview_image)
    ImageView shotPreviewImage;
    @BindView(R.id.author_avatar_circle_image)
    CircleImageView authorAvatarCircleImage;
    @BindView(R.id.image_title_text_view)
    TextView imageTitleTextView;
    @BindView(R.id.date_text_view)
    TextView inAndDateTextView;
    @BindView(R.id.author_text_view)
    TextView authorTextView;
    @BindView(R.id.buckets_recycler_view)
    RecyclerView bucketsRecyclerView;
    @BindView(R.id.bucket_list_progress_bar)
    ProgressBar bucketListProgressBar;
    @BindView(R.id.no_buckets_text)
    TextView noBucketsText;

    @BindColor(R.color.pink)
    int pinkColor;

    @BindString(R.string.fragment_add_to_bucket_shot_created_on)
    String shotCreatedOnString;
    @BindString(R.string.fragment_add_to_bucket_shot_created_by)
    String shotCreatedByString;
    @BindString(R.string.fragment_add_to_bucket_shot_created_for)
    String shotCreatedForString;

    private final BucketsAdapter bucketsAdapter = new BucketsAdapter(this);

    public static AddToBucketDialogFragment newInstance(@NonNull Fragment targetFragment, @NonNull Shot shot) {
        Bundle args = new Bundle();
        args.putParcelable(SHOT_ARG_KEY, shot);

        AddToBucketDialogFragment fragment = new AddToBucketDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, FRAGMENT_REQUEST_CODE);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BigDialog);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_to_bucket, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bucketsRecyclerView.setAdapter(bucketsAdapter);
        bucketsRecyclerView.addItemDecoration(new DividerItemDecorator(getContext(), DividerItemDecorator.Orientation.VERTICAL_LIST));
        getPresenter().handleShot(getArguments().getParcelable(SHOT_ARG_KEY));
        getPresenter().loadAvailableBuckets();
    }

    @NonNull
    @Override
    public AddToBucketContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusAddToBucketComponent().getPresenter();
    }

    @OnClick(R.id.close_image)
    void onCloseButtonClick() {
        dismiss();
    }

    @OnClick(R.id.shot_preview_image)
    void onOpenShotFullscreen() {
        presenter.onOpenShotFullscreen();
    }

    @Override
    public void onBucketClick(Bucket bucket) {
        getPresenter().handleBucketClick(bucket);
    }

    @Override
    public void setShotTitle(String title) {
        imageTitleTextView.setText(title);
    }

    @Override
    public void showAuthorAvatar(String url) {
        Glide.with(this)
                .load(url)
                .into(authorAvatarCircleImage);
    }

    @Override
    public void showShotPreview(String url) {
        Glide.with(this)
                .load(url)
                .into(shotPreviewImage);
    }

    @Override
    public void showShotAuthor(String authorName) {
        authorTextView.setText(getFormattedAuthor(authorName), TextView.BufferType.SPANNABLE);
    }

    private SpannableStringBuilder getFormattedAuthor(String authorName) {
        return TextFormatterUtil.changeColourOfConcatenatedWord(shotCreatedByString, authorName, pinkColor);
    }

    @Override
    public void showShotAuthorAndTeam(String authorName, String teamName) {
        SpannableStringBuilder spannableStringAuthorBuilder = getFormattedAuthor(authorName);
        SpannableStringBuilder spannableStringTeamBuilder = TextFormatterUtil.changeColourOfConcatenatedWord(
                shotCreatedForString,
                teamName,
                pinkColor);
        SpannableStringBuilder result = spannableStringAuthorBuilder.append(' ').append(spannableStringTeamBuilder);
        authorTextView.setText(result, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void showShotCreationDate(LocalDateTime localDateTime) {
        inAndDateTextView.setText(getFormattedDate(localDateTime));
    }

    @Override
    public void showApiError() {
        Toast.makeText(getContext(), R.string.undefined_api_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showBucketListLoading() {
        bucketListProgressBar.setVisibility(View.VISIBLE);
        noBucketsText.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoBucketsAvailable() {
        bucketListProgressBar.setVisibility(View.GONE);
        noBucketsText.setVisibility(View.VISIBLE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showBuckets(List<Bucket> buckets) {
        bucketListProgressBar.setVisibility(View.GONE);
        noBucketsText.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.VISIBLE);
        bucketsAdapter.showBucketsList(buckets);
    }

    @Override
    public void passResultAndCloseFragment(Bucket bucket, Shot shot) {
        Fragment targetFragment = getTargetFragment();
        try {
            BucketSelectListener listener = (BucketSelectListener) targetFragment;
            listener.onBucketForShotSelect(bucket, shot);
            dismiss();
        } catch (ClassCastException e) {
            throw new ClassCastException(targetFragment.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void openShotFullscreen(Shot shot) {
        ShotFullscreenActivity.startActivity(getContext(), shot, Collections.emptyList(),
                ShotDetailsRequest.create(ShotDetailsType.ADD_TO_BUCKET));
    }

    private String getFormattedDate(LocalDateTime creationDate) {
        return String.format(shotCreatedOnString,
                DateTimeFormatUtil.getMonthShortDayAndYearFormattedDate(creationDate));
    }

    @FunctionalInterface
    public interface BucketSelectListener {

        void onBucketForShotSelect(Bucket bucket, Shot shot);
    }
}
