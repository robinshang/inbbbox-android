package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public class UserShotHorizontalViewHolder extends BaseViewHolder<Shot> {

    private final ShotClickListener shotClickListener;

    @BindView(R.id.shot_image)
    RoundedCornersShotImageView shotImageView;

    private Shot item;

    public UserShotHorizontalViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_shot, parent, false));
        this.shotClickListener = shotClickListener;
    }

    @OnClick(R.id.shot_image)
    void onShotImageClick() {
        shotClickListener.onShotClick(item);
    }

    @Override
    public void bind(Shot item) {
        this.item = item;
        shotImageView.loadShot(item);
    }
}