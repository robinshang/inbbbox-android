package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.common.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpDialogFragment;
import co.netguru.android.inbbbox.feature.shared.view.DividerItemDecorator;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenActivity;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.adapter.BucketViewHolderRemoveFromBucket;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.adapter.BucketsAdapterRemoveFromBucket;
import de.hdodenhof.circleimageview.CircleImageView;

public class RemoveFromBucketDialogFragment
        extends BaseMvpDialogFragment<RemoveFromBucketContract.View,
        RemoveFromBucketContract.Presenter>
        implements RemoveFromBucketContract.View,
        BucketViewHolderRemoveFromBucket.CheckboxChangeListener {

    public static final String TAG = RemoveFromBucketDialogFragment.class.getSimpleName();

    private static final String SHOT_ARG_KEY = "shot_arg_key";
    private static final int FRAGMENT_REQUEST_CODE = 1;
    private static final int LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE = 10;

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

    float heightPercentage;

    @BindString(R.string.fragment_add_to_bucket_shot_created_on)
    String shotCreatedOnString;
    @BindString(R.string.fragment_add_to_bucket_shot_created_by)
    String shotCreatedByString;
    @BindString(R.string.fragment_add_to_bucket_shot_created_for)
    String shotCreatedForString;

    private final BucketsAdapterRemoveFromBucket bucketsAdapter
            = new BucketsAdapterRemoveFromBucket(this);

    public static RemoveFromBucketDialogFragment newInstance(@NonNull Fragment targetFragment,
                                                             @NonNull Shot shot) {
        Bundle args = new Bundle();
        args.putParcelable(SHOT_ARG_KEY, shot);

        RemoveFromBucketDialogFragment fragment = new RemoveFromBucketDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, FRAGMENT_REQUEST_CODE);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BigDialog);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove_from_bucket, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heightPercentage = getResources()
                .getFraction(R.fraction.big_dialog_height_width_percentage, 1, 1);
        setupRecyclerView();
        getPresenter().handleShot(getArguments().getParcelable(SHOT_ARG_KEY));
        getPresenter().loadBucketsForShot();
    }

    @Override
    public void onStart() {
        setupDialogHeight();
        super.onStart();
    }

    @NonNull
    @Override
    public RemoveFromBucketContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusRemoveFromBucketComponent().getPresenter();
    }

    @OnClick(R.id.close_image)
    void onCloseButtonClick() {
        dismiss();
    }

    @OnClick(R.id.shot_preview_image)
    void onOpenShotFullscreen() {
        getPresenter().onOpenShotFullscreen();
    }

    @OnClick(R.id.remove_from_bucket_button)
    void onRemoveFromBucketButtonClick() {
        getPresenter().removeShotFromBuckets();
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
        return TextFormatterUtil.changeColourOfConcatenatedWord(shotCreatedByString,
                authorName,
                pinkColor);
    }

    @Override
    public void showShotAuthorAndTeam(String authorName, String teamName) {
        SpannableStringBuilder spannableStringAuthorBuilder = getFormattedAuthor(authorName);
        SpannableStringBuilder spannableStringTeamBuilder = TextFormatterUtil
                .changeColourOfConcatenatedWord(
                shotCreatedForString,
                teamName,
                pinkColor);
        SpannableStringBuilder result = spannableStringAuthorBuilder.append(' ')
                .append(spannableStringTeamBuilder);
        authorTextView.setText(result, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void showShotCreationDate(ZonedDateTime dateTime) {
        inAndDateTextView.setText(getFormattedDate(dateTime));
    }

    @Override
    public void showBucketListLoading() {
        bucketListProgressBar.setVisibility(View.VISIBLE);
        noBucketsText.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setBucketsList(List<Bucket> buckets) {
        bucketsAdapter.setBuckets(buckets);
    }

    @Override
    public void hideProgressBar() {
        bucketListProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void passResultAndCloseFragment(List<Bucket> list, Shot shot) {
        Fragment targetFragment = getTargetFragment();
        try {
            RemoveFromBucketDialogFragment.BucketSelectListener listener
                    = (RemoveFromBucketDialogFragment.BucketSelectListener) targetFragment;
            listener.onBucketToRemoveFromForShotSelect(list, shot);
            dismiss();
        } catch (ClassCastException e) {
            throw new InterfaceNotImplementedException(e,
                    targetFragment.getClass().getSimpleName(),
                    RemoveFromBucketDialogFragment.BucketSelectListener.class.getSimpleName());
        }
    }

    @Override
    public void openShotFullscreen(Shot shot) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.REMOVE_FROM_BUCKET)
                .build();

        ShotFullscreenActivity.startActivitySingleShot(getContext(), shot, request);
    }

    @Override
    public void showBucketListLoadingMore() {
        showMessageOnSnackbar(getResources().getString(R.string.loading_more_buckets));
    }

    @Override
    public void showMoreBuckets(List<Bucket> buckets) {
        bucketsAdapter.addMoreBuckets(buckets);
    }

    @Override
    public void showBucketsList() {
        noBucketsText.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showMessageOnSnackbar(errorText);
    }

    private void setupRecyclerView() {
        bucketsRecyclerView.setAdapter(bucketsAdapter);
        bucketsRecyclerView.addItemDecoration(
                new DividerItemDecorator(getContext(),
                        DividerItemDecorator.Orientation.VERTICAL_LIST, false));

        bucketsRecyclerView.addOnScrollListener(
                new LoadMoreScrollListener(LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE) {
                    @Override
                    public void requestMoreData() {
                        getPresenter().loadMoreBuckets();
                    }
                });
    }

    /**
     * This method set dialog height to fixed size (heightPercentage * windowHeight) instead of wrap content,
     * fixing problems with wrong height calculation for inflated layout.
     **/
    private void setupDialogHeight() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(window.getAttributes().width, Math.round(size.y * heightPercentage));
    }

    private String getFormattedDate(ZonedDateTime creationDate) {
        return String.format(shotCreatedOnString,
                DateTimeFormatUtil.getMonthShortDayAndYearFormattedDate(creationDate));
    }

    @Override
    public void onCheckboxChange(Bucket bucket, boolean isChecked) {
        getPresenter().handleCheckboxClick(bucket, isChecked);
    }

    @FunctionalInterface
    public interface BucketSelectListener {

        void onBucketToRemoveFromForShotSelect(List<Bucket> list, Shot shot);
    }
}
