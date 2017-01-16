package co.netguru.android.inbbbox.feature.shot.removefrombucket.adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

public class BucketViewHolderRemoveFromBucket extends BaseViewHolder<Bucket> {

    @BindView(R.id.bucket_name_text_view)
    TextView bucketNameText;
    @BindView(R.id.shots_count_text_view)
    TextView shotsCountText;
    @BindView(R.id.checkbox_linear_layout)
    LinearLayout checkboxLinearLayout;
    @BindView(R.id.bucket_checkbox)
    CheckBox bucketCheckbox;
    @BindString(R.string.fragment_add_to_bucket_shots)
    String shotsCountString;

    private CheckboxChangeListener checkboxChangeListener;

    public BucketViewHolderRemoveFromBucket(ViewGroup parent, @NonNull CheckboxChangeListener checkboxChangeListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_bucket_list_item, parent, false));
        this.checkboxChangeListener = checkboxChangeListener;
    }

    @Override
    public void bind(Bucket item) {
        bucketNameText.setText(item.name());
        shotsCountText.setText(String.format(shotsCountString, item.shotsCount()));
        checkboxLinearLayout.setOnClickListener(view -> {
                    bucketCheckbox.setChecked(!bucketCheckbox.isChecked());
                    checkboxChangeListener.onCheckboxChange(item, bucketCheckbox.isChecked());
        });
    }

    @FunctionalInterface
    public interface CheckboxChangeListener {
        void onCheckboxChange(Bucket bucket, boolean isChecked);
    }
}
