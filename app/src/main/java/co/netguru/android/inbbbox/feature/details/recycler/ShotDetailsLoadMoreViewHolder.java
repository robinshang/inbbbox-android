package co.netguru.android.inbbbox.feature.details.recycler;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;

import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsLoadMoreViewHolder extends ShotDetailsViewHolder {

    ShotDetailsLoadMoreViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @OnClick(R.id.shot_details_load_more_textView)
    void onLoadMoreSelected() {
        Toast.makeText(itemView.getContext(), "LOAD MORE", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleBinding() {
    }
}
