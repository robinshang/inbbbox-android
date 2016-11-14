package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;

public class ShotDetailsDescriptionViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.shot_details_description_textView)
    TextView descriptionTextView;

    public ShotDetailsDescriptionViewHolder(View view) {
        super(view);
    }

    @Override
    protected void handleBinding() {
        descriptionTextView.setText(item.description());
    }
}
