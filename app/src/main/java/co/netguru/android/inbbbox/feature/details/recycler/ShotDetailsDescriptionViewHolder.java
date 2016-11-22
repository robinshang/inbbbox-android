package co.netguru.android.inbbbox.feature.details.recycler;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;

import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsDescriptionViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_description_textView)
    TextView descriptionTextView;

    ShotDetailsDescriptionViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @Override
    protected void handleBinding() {
        if (item.description() != null) {
            descriptionTextView.setText(getParsedHtmlTextSpanned(item.description()));
            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            descriptionTextView.setVisibility(View.GONE);
        }
    }
}
