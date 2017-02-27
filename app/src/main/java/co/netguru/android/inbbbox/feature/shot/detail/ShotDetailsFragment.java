package co.netguru.android.inbbbox.feature.shot.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.utils.AnimationUtil;
import co.netguru.android.inbbbox.common.utils.InputUtil;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.shot.model.ui.ShotImage;
import co.netguru.android.inbbbox.feature.user.UserActivity;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenActivity;
import co.netguru.android.inbbbox.feature.shot.detail.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.shot.detail.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.RemoveFromBucketDialogFragment;

public class ShotDetailsFragment
        extends BaseMvpFragment<ShotDetailsContract.View, ShotDetailsContract.Presenter>
        implements ShotDetailsContract.View, DetailsViewActionCallback,
        AddToBucketDialogFragment.BucketSelectListener, RemoveFromBucketDialogFragment.BucketSelectListener {

    public static final String TAG = ShotDetailsFragment.class.getSimpleName();
    private static final String ARG_ALL_SHOTS = "arg:all_shots";
    private static final String ARG_SHOT = "arg:shot";
    private static final String ARG_SHOT_DETAIL_REQUEST = "arg:detail_request";
    private static final int SLIDE_IN_DURATION = 500;

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    RoundedCornersShotImageView parallaxImageView;

    @BindView(R.id.comment_input_panel)
    View shotCommentInputPanel;

    @BindView(R.id.comment_textInputLayout)
    TextInputLayout commentTextInputLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.send_progressBar)
    ProgressBar sendProgressBar;

    @BindView(R.id.comment_send_imageView)
    View sendButton;

    private ShotDetailsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isInputPanelShowingEnabled;

    public static ShotDetailsFragment newInstance(Shot shot, List<Shot> allShots,
                                                  ShotDetailsRequest detailsRequest) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_SHOT, shot);
        args.putParcelable(ARG_SHOT_DETAIL_REQUEST, detailsRequest);
        if (allShots instanceof ArrayList) {
            args.putParcelableArrayList(ARG_ALL_SHOTS, (ArrayList<Shot>) allShots);
        } else {
            args.putParcelableArrayList(ARG_ALL_SHOTS, new ArrayList<>(allShots));
        }
        ShotDetailsFragment fragment = new ShotDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater
                .from(getContext())
                .inflate(R.layout.fragment_shot_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        adapter = new ShotDetailsAdapter(this);
        getPresenter().retrieveInitialData();
        getPresenter().downloadData();
        getPresenter().checkShotBucketsCount(getArguments().getParcelable(ARG_SHOT));
    }

    @NonNull
    @Override
    public ShotDetailsContract.Presenter createPresenter() {
        List<Shot> shots = getArguments().getParcelableArrayList(ARG_ALL_SHOTS);

        return App.getUserComponent(getContext()).plus(new ShotsDetailsModule(shots)).getPresenter();
    }

    @OnClick(R.id.comment_send_imageView)
    void onSendCommentClick() {
        getPresenter().sendComment();
    }

    @OnClick(R.id.details_close_imageView)
    void onCloseClick() {
        getPresenter().closeScreen();
    }

    @OnClick(R.id.parallax_image_view)
    void onShotImageClick() {
        getPresenter().onShotImageClick();
    }

    @Override
    public void initView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        shotRecyclerView.setLayoutManager(linearLayoutManager);
        shotRecyclerView.addOnScrollListener(createScrollListener());
        shotRecyclerView.setAdapter(adapter);
        appBarLayout.addOnOffsetChangedListener((layout, offset) -> verifyInputVisibility());
    }

    @Override
    public Shot getShotInitialData() {
        return getArguments().getParcelable(ARG_SHOT);
    }

    @Override
    public String getCommentText() {
        return commentTextInputLayout.getEditText().getText().toString();
    }

    @Override
    public void setInputShowingEnabled(boolean isInputPanelShowingEnabled) {
        this.isInputPanelShowingEnabled = isInputPanelShowingEnabled;
    }

    @Override
    public boolean getCommentModeInitialState() {
        return ((ShotDetailsRequest) getArguments().getParcelable(ARG_SHOT_DETAIL_REQUEST)).isCommentModeEnabled();
    }

    @Override
    public void scrollToLastItem() {
        shotRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void collapseAppbarWithAnimation() {
        appBarLayout.setExpanded(false, true);
    }

    @Override
    public void showKeyboard() {
        InputUtil.showKeyboard(getContext(), shotRecyclerView);
    }

    @Override
    public void showCommentEditorDialog(String text) {
        EditCommentFragmentDialog
                .newInstance(this, text)
                .show(getFragmentManager(), EditCommentFragmentDialog.TAG);
    }

    @Override
    public void showDetails(Shot details) {
        adapter.setDetails(details);
    }

    @Override
    public void addCommentsToList(List<Comment> commentList) {
        verifyInputVisibility();
        adapter.addComments(commentList);
    }

    @Override
    public void onTeamSelected(Team team) {
        getPresenter().getTeamUserWithShots(team);
    }

    @Override
    public void onUserSelected(User user) {
        UserActivity.startActivity(getContext(), user);
    }

    @Override
    public void onShotLikeAction(boolean newLikeState) {
        getPresenter().handleShotLike(newLikeState);
    }

    @Override
    public void onShotBucket(long shotId) {
        getPresenter().onShotBucketClicked(getArguments().getParcelable(ARG_SHOT));
    }

    @Override
    public void onLoadMoreCommentsSelected() {
        getPresenter().getMoreComments();
    }

    @Override
    public void onCommentDeleteSelected(Comment currentComment) {
        getPresenter().onCommentDelete(currentComment);
    }

    @Override
    public void onCommentEditSelected(Comment currentComment) {
        getPresenter().onEditCommentClick(currentComment);
    }

    @Override
    public void onCommentUpdated(String comment) {
        getPresenter().updateComment(comment);
    }

    @Override
    public void onCommentDeleteConfirmed() {
        getPresenter().onCommentDeleteConfirmed();
    }

    @Override
    public void showMainImage(ShotImage shotImage) {
        ShotLoadingUtil.loadMainViewShot(getContext(), parallaxImageView.getImageView(), shotImage);
    }

    @Override
    public void showInputIfHidden() {
        if (shotCommentInputPanel != null && shotCommentInputPanel.getVisibility() == View.GONE) {
            AnimationUtil.startSlideInFromBottomShowAnimation(shotCommentInputPanel,
                    SLIDE_IN_DURATION);
        }
    }

    @Override
    public void requestFocusOnCommentInput() {
        commentTextInputLayout.requestFocus();
    }

    @Override
    public void hideDetailsScreen() {
        getActivity().onBackPressed();
    }

    @Override
    public void hideKeyboard() {
        InputUtil.hideKeyboard(getContext(), shotRecyclerView);
    }

    @Override
    public void showSendingCommentIndicator() {
        sendProgressBar.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.GONE);
    }

    @Override
    public void hideSendingCommentIndicator() {
        sendProgressBar.setVisibility(View.GONE);
        sendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewComment(Comment updatedComment) {
        int indexOfInsertedComment = adapter.addComment(updatedComment);
        shotRecyclerView.smoothScrollToPosition(indexOfInsertedComment);
    }

    @Override
    public void clearCommentInput() {
        commentTextInputLayout.getEditText().setText("");
    }

    @Override
    public void showDeleteCommentWarning() {
        RemoveCommentFragmentDialog
                .newInstance(this)
                .show(getFragmentManager(), RemoveCommentFragmentDialog.TAG);
    }

    @Override
    public void showInfo(@StringRes int messageResId) {
        showTextOnSnackbar(getResources().getString(messageResId));
    }

    @Override
    public void removeCommentFromView(Comment commentInEditor) {
        adapter.removeComment(commentInEditor);
    }

    @Override
    public void updateLoadMoreState(CommentLoadMoreState commentLoadMoreState) {
        adapter.updateLoadMoreState(commentLoadMoreState);
    }

    @Override
    public void dismissCommentEditor() {
        EditCommentFragmentDialog editorFragmentDialog = (EditCommentFragmentDialog) getFragmentManager()
                .findFragmentByTag(EditCommentFragmentDialog.TAG);
        if (editorFragmentDialog != null) {
            editorFragmentDialog.dismiss();
        }
    }

    @Override
    public void updateComment(Comment commentToUpdate, Comment updatedComment) {
        adapter.replaceComment(commentToUpdate, updatedComment);
    }

    @Override
    public void disableEditorProgressMode() {
        EditCommentFragmentDialog editorFragmentDialog = (EditCommentFragmentDialog) getFragmentManager()
                .findFragmentByTag(EditCommentFragmentDialog.TAG);
        if (editorFragmentDialog != null) {
            editorFragmentDialog.disableProgressMode();
        }
    }

    @Override
    public void openShotFullscreen(List<Shot> allShots, int previewShotIndex) {
        ShotDetailsRequest detailsRequest = getArguments().getParcelable(ARG_SHOT_DETAIL_REQUEST);
        ShotFullscreenActivity.startActivity(getContext(), allShots, previewShotIndex, detailsRequest);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showTextOnSnackbar(errorText);
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        getPresenter().addShotToBucket(bucket, shot);
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void showShotRemoveFromBucketSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_remove_shot_from_bucket_success);
    }

    @Override
    public void showAddShotToBucketView(Shot shot) {
        AddToBucketDialogFragment
                .newInstance(this, shot)
                .show(getFragmentManager(), AddToBucketDialogFragment.TAG);
    }

    @Override
    public void showRemoveShotFromBucketView(Shot shot) {
        RemoveFromBucketDialogFragment
                .newInstance(this, shot)
                .show(getFragmentManager(), RemoveFromBucketDialogFragment.TAG);
    }

    @Override
    public void showTeamView(UserWithShots userWithShots) {
        UserActivity.startActivity(getContext(), userWithShots.user());
    }

    private RecyclerView.OnScrollListener createScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                verifyInputVisibility();
            }
        };
    }

    private void verifyInputVisibility() {
        int lastVisibleIndex = linearLayoutManager.findLastVisibleItemPosition();
        if (isInputPanelShowingEnabled && adapter.isInputVisibilityPermitted(lastVisibleIndex)) {
            showInputIfHidden();
        }
    }

    @Override
    public void onBucketToRemoveFromForShotSelect(List<Bucket> list, Shot shot) {
        getPresenter().removeShotFromBuckets(list, shot);
    }
}
