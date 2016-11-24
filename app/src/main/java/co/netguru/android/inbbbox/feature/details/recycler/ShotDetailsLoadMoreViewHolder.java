package co.netguru.android.inbbbox.feature.details.recycler;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;

import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsLoadMoreViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_load_more_textView)
    TextView loadMoreTextView;

    @BindString(R.string.load_more_label)
    String loadMoreLabel;

    ShotDetailsLoadMoreViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @OnClick(R.id.shot_details_load_more_textView)
    void onLoadMoreSelected() {
        actionCallbackListener.onLoadMoreCommentsSelected();
    }

    @Override
    protected void handleBinding() {
        boolean isActive = commentList != null && commentList.size() < item.commentsCount();
        loadMoreTextView
                .setActivated(isActive);
        loadMoreTextView.setClickable(isActive);

        if (loadMoreTextView.isActivated()) {
            loadMoreTextView.setText(loadMoreLabel);
        } else {
            loadMoreTextView.setText("");
        }
    }
}
