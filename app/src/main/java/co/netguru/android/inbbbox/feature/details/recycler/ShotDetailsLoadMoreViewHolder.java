package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;

class ShotDetailsLoadMoreViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_load_more_textView)
    TextView loadMoreTextView;

    @BindView(R.id.load_more_progressBar)
    ProgressBar loadMoreProgressBar;

    @BindString(R.string.load_more_label)
    String loadMoreLabel;

    private boolean isLoadingActive;

    ShotDetailsLoadMoreViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @OnClick(R.id.shot_details_load_more_textView)
    void onLoadMoreSelected() {
        loadMoreProgressBar.setVisibility(View.VISIBLE);
        actionCallbackListener.onLoadMoreCommentsSelected();
        isLoadingActive = false;
        updateButtonState();
    }

    @Override
    protected void handleBinding() {
        // TODO: 25.11.2016 handle loader initial state
        isLoadingActive = commentList != null && commentList.size() < item.commentsCount();
        loadMoreProgressBar.setVisibility(View.GONE);
        updateButtonState();
    }

    private void updateButtonState() {
        loadMoreTextView
                .setActivated(isLoadingActive);
        loadMoreTextView.setClickable(isLoadingActive);

        if (loadMoreTextView.isActivated()) {
            loadMoreTextView.setText(loadMoreLabel);
        } else {
            loadMoreTextView.setText("");
        }
    }
}
