package co.netguru.android.inbbbox.feature.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotDetailsComponent;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.CommentLoadMoreState;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotImage;
import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.inbbbox.utils.AnimationUtil;
import co.netguru.android.inbbbox.utils.InputUtils;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;


public class ShotDetailsFragment
        extends BaseMvpFragment<ShotDetailsContract.View, ShotDetailsContract.Presenter>
        implements ShotDetailsContract.View, DetailsViewActionCallback {

    public static final String TAG = ShotDetailsFragment.class.getSimpleName();
    private static final String ARG_SHOT = "arg:shot";
    private static final String ARG_IS_COMMENT_MODE_ENABLED = "arg:comment_mode_state";

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    RoundedCornersImageView parallaxImageView;

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

    @BindDimen(R.dimen.shot_corner_radius)
    int radius;

    @Inject
    ShotDetailsAdapter adapter;

    private ShotDetailsComponent component;
    private LinearLayoutManager linearLayoutManager;
    private boolean isInputPanelShowingEnabled;

    public static ShotDetailsFragment newInstance(Shot shot) {
        return newInstance(shot, false);
    }

    public static ShotDetailsFragment newInstance(Shot shot, boolean isCommentModeEnabled) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_SHOT, shot);
        args.putBoolean(ARG_IS_COMMENT_MODE_ENABLED, isCommentModeEnabled);
        ShotDetailsFragment fragment = new ShotDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initComponent();
        return LayoutInflater
                .from(getContext())
                .inflate(R.layout.fragment_shot_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().retrieveInitialData();
        getPresenter().downloadData();
    }

    @NonNull
    @Override
    public ShotDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @OnClick(R.id.comment_send_imageView)
    void onSendCommentClick() {
        getPresenter().sendComment();
    }

    @OnClick(R.id.details_close_imageView)
    void onCloseClick() {
        getPresenter().closeScreen();
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
    public void showErrorMessage(String errorMessageLabel) {
        Toast.makeText(getContext(), errorMessageLabel, Toast.LENGTH_LONG).show();
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
        return getArguments().getBoolean(ARG_IS_COMMENT_MODE_ENABLED);
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
        InputUtils.showKeyboard(getContext(), shotRecyclerView);
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
    public void showComments(List<Comment> commentList) {
        verifyInputVisibility();
        adapter.addComments(commentList);
    }

    @Override
    public void onTeamSelected(Team team) {
        // TODO: 15.11.2016 not in scope of this task
        Toast.makeText(getContext(), "team clicked: " + team.name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserSelected(User user) {
        // TODO: 15.11.2016 not in scope of this task
        Toast.makeText(getContext(), "user clicked " + user.name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShotLikeAction(boolean newLikeState) {
        getPresenter().handleShotLike(newLikeState);
    }

    @Override
    public void onShotBucket(long shotId, boolean isLikedBucket) {
        // TODO: 15.11.2016 not in scope of this task
        Toast.makeText(getContext(), "bucket: " + isLikedBucket, Toast.LENGTH_SHORT).show();
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
        parallaxImageView.setRadius(radius);
        parallaxImageView.disableRadiusForBottomEdge(true);
        ShotLoadingManager.loadMainViewShot(getContext(), parallaxImageView, shotImage);
    }

    @Override
    public void showInputIfHidden() {
        if (shotCommentInputPanel != null && shotCommentInputPanel.getVisibility() == View.GONE) {
            AnimationUtil.startSlideInFromBottomShowAnimation(shotCommentInputPanel);
        }
    }

    @Override
    public void hideDetailsScreen() {
        getActivity().onBackPressed();
    }

    @Override
    public void hideKeyboard() {
        InputUtils.hideKeyboard(getContext(), shotRecyclerView);
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
    public void showCommentDeletedInfo() {
        Toast.makeText(getContext(),
                getString(R.string.comment_deleted_complete),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void removeCommentFromView(Comment commentInEditor) {
        adapter.removeComment(commentInEditor);
    }

    @Override
    public void updateLoadMoreState(CommentLoadMoreState commentLoadMoreState) {
        adapter.updateLoadMoreState(commentLoadMoreState);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plus(new ShotsDetailsModule(this));
        component.inject(this);
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
}
