package co.netguru.android.inbbbox.feature.shot.addtobucket.adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.api.Bucket;

public class BucketViewHolder extends BaseViewHolder<Bucket> {

    @BindView(R.id.bucket_name_text_view)
    TextView bucketNameText;
    @BindView(R.id.shots_count_text_view)
    TextView shotsCountText;
    @BindString(R.string.fragment_add_to_bucket_shots)
    String shotsCountString;

    private Bucket currentBucket;

    public BucketViewHolder(ViewGroup parent, @NonNull BucketClickListener bucketClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item, parent, false));
        itemView.setOnClickListener(v -> bucketClickListener.onBucketClick(currentBucket));
    }

    @Override
    public void bind(Bucket item) {
        currentBucket = item;
        bucketNameText.setText(currentBucket.name());
        shotsCountText.setText(String.format(shotsCountString, currentBucket.shotsCount()));
    }

    @FunctionalInterface
    public interface BucketClickListener {
        void onBucketClick(Bucket bucket);
    }
}
