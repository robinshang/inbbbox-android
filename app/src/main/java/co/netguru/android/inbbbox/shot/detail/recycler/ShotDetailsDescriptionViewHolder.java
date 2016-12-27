package co.netguru.android.inbbbox.shot.detail.recycler;

import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

import static co.netguru.android.inbbbox.common.utils.StringUtil.getParsedHtmlTextSpanned;

class ShotDetailsDescriptionViewHolder extends ShotDetailsViewHolder<Shot> {

    @BindView(R.id.shot_details_description_textView)
    TextView descriptionTextView;

    ShotDetailsDescriptionViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback) {
        super(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_info_description_layout, parent, false),
                actionCallback);
    }

    @Override
    public void bind(@NonNull Shot item) {
        if (item.description() != null) {
            descriptionTextView.setText(getParsedHtmlTextSpanned(item.description()));
            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            descriptionTextView.setHint("No description");
        }
    }
}
