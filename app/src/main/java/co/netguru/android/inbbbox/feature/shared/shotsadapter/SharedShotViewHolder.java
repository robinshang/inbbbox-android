package co.netguru.android.inbbbox.feature.shared.shotsadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public class SharedShotViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.shared_shot_item_view)
    RoundedCornersShotImageView shotImageView;

    private Shot currentShot;

    SharedShotViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_shot_item, parent, false));
        shotImageView.setOnClickListener(v -> shotClickListener.onShotClick(currentShot));
    }

    @Override
    public void bind(Shot item) {
        currentShot = item;
        shotImageView.loadShot(item);
    }
}