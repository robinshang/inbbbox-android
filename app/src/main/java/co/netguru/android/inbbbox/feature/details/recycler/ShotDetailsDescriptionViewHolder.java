package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;

import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsDescriptionViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_description_textView)
    TextView descriptionTextView;

    ShotDetailsDescriptionViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback) {
        super(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_shot_info_description_layout, parent, false),
                actionCallback);
    }

    @Override
    public void bind(@Nullable Shot item, @Nullable Comment comments) {
        if (item.description() != null) {
            descriptionTextView.setText(getParsedHtmlTextSpanned(item.description()));
            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            descriptionTextView.setHint("No description");
        }
    }
}
