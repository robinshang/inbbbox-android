package co.netguru.android.inbbbox.feature.shots.addtobucket;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpDialogFragment;
import co.netguru.android.inbbbox.feature.shots.addtobucket.adapter.BucketViewHolder;
import co.netguru.android.inbbbox.feature.shots.addtobucket.adapter.BucketsAdapter;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.DateFormatUtil;
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

    private final BucketsAdapter bucketsAdapter = new BucketsAdapter(this);

    private Shot shot;

    public static AddToBucketDialogFragment newInstance(@NonNull Fragment targetFragment, @NonNull Shot shot) {
        Bundle args = new Bundle();
        args.putParcelable(SHOT_ARG_KEY, shot);

        AddToBucketDialogFragment fragment = new AddToBucketDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, FRAGMENT_REQUEST_CODE);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BigDialog);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.shot = getArguments().getParcelable(SHOT_ARG_KEY);
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
        getPresenter().handleShot(shot);
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
        authorTextView.setText(getSpannableAuthorStringBuilder(authorName), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void showShotAuthorAndTeam(String authorName, String teamName) {
        SpannableStringBuilder spannableStringBuilder = getSpannableAuthorStringBuilder(authorName);
        spannableStringBuilder
                .append(' ').append(getString(R.string.fragment_add_to_bucket_shot_created_for))
                .append(' ').append(teamName)
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.pink)),
                        spannableStringBuilder.length() - teamName.length(), spannableStringBuilder.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
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
    public void passResultAndCloseFragment(Bucket bucket) {
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

    private String getFormattedDate(LocalDateTime creationDate) {
        return String.format(getString(R.string.fragment_add_to_bucket_shot_created_on), DateFormatUtil.getMonthShortDayAndYearFormatedDate(creationDate));
    }

    @NonNull
    private SpannableStringBuilder getSpannableAuthorStringBuilder(String authorName) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.fragment_add_to_bucket_shot_created_by));
        spannableStringBuilder
                .append(' ')
                .append(authorName)
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.pink)),
                        spannableStringBuilder.length() - authorName.length(), spannableStringBuilder.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    @FunctionalInterface
    public interface BucketSelectListener {

        void onBucketForShotSelect(Bucket bucket, Shot shot);
    }
}
