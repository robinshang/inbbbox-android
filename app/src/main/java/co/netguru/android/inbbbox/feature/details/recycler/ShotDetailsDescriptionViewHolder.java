package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;

class ShotDetailsDescriptionViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_description_textView)
    TextView descriptionTextView;

    ShotDetailsDescriptionViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @Override
    protected void handleBinding() {
        descriptionTextView.setText(item.description());
    }
}
