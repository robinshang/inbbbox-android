package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shot.detail.CommentLoadMoreState;

class ShotDetailsLoadMoreViewHolder extends ShotDetailsViewHolder<CommentLoadMoreState> {

    @BindView(R.id.load_more_progressBar)
    ProgressBar loadMoreProgressBar;
    @BindView(R.id.load_more_text_container)
    FrameLayout loadMoreTextContainer;
    @BindView(R.id.shot_details_load_more_textView)
    TextView loadMoreTextView;

    private CommentLoadMoreState loadMoreState;

    ShotDetailsLoadMoreViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback) {
        super(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_load_more_layout, parent, false),
                actionCallback);
    }

    @OnClick(R.id.shot_details_load_more_textView)
    void onLoadMoreSelected() {
        actionCallbackListener.onLoadMoreCommentsSelected();
    }

    @Override
    public void bind(@NonNull CommentLoadMoreState item) {
        this.loadMoreState = item;
        loadMoreProgressBar.setVisibility(View.GONE);
        updateButtonState();
        updateLoaderState();
    }

    private void updateLoaderState() {
        if (loadMoreState.isWaitingForUpdate()) {
            loadMoreProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadMoreProgressBar.setVisibility(View.GONE);
        }
    }

    private void updateButtonState() {
        loadMoreTextView
                .setActivated(loadMoreState.isLoadMoreActive());
        loadMoreTextView.setClickable(loadMoreState.isLoadMoreActive());
        loadMoreTextContainer.setVisibility(canLoadMoreComments() ? View.VISIBLE : View.GONE);
    }

    private boolean canLoadMoreComments() {
        return !loadMoreState.isWaitingForUpdate() && loadMoreState.isLoadMoreActive();
    }
}
