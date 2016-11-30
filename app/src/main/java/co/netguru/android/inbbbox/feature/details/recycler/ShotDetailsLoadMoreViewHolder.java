package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Shot;

class ShotDetailsLoadMoreViewHolder extends ShotDetailsViewHolder<Shot> {

    @BindView(R.id.shot_details_load_more_textView)
    TextView loadMoreTextView;

    @BindView(R.id.load_more_progressBar)
    ProgressBar loadMoreProgressBar;

    @BindString(R.string.load_more_label)
    String loadMoreLabel;

    // TODO: 30.11.2016 loadingState will be handled in IA-41
    private boolean isLoadingActive;

    ShotDetailsLoadMoreViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback) {
        super(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_load_more_layout, parent, false),
                actionCallback);
    }

    @OnClick(R.id.shot_details_load_more_textView)
    void onLoadMoreSelected() {
        loadMoreProgressBar.setVisibility(View.VISIBLE);
        actionCallbackListener.onLoadMoreCommentsSelected();
        isLoadingActive = false;
        updateButtonState();
    }

    @Override
    public void bind(@NonNull Shot item) {
        // TODO: 25.11.2016 handle loader initial state - in logic task
        //// TODO: 30.11.2016 bining with item state will be implemented in IA-41
        isLoadingActive = false;
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
