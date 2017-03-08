package co.netguru.android.inbbbox.feature.user.projects.adapter.shots;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.user.projects.adapter.ProjectClickListener;

public class ProjectShotsViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.project_shot_item_image_view)
    RoundedCornersShotImageView shotImageView;

    private ShotClickListener shotClickListener;
    private Shot shot;

    ProjectShotsViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_shot_item, parent, false));
        this.shotClickListener = shotClickListener;
    }

    @OnClick(R.id.project_shot_item_image_view)
    void onShotClick() {
        shotClickListener.onShotClick(shot);
    }

    @Override
    public void bind(Shot shot) {
        this.shot = shot;
        shotImageView.loadShot(shot);
    }
}
