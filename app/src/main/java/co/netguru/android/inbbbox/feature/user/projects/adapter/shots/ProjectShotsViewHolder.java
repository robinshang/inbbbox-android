package co.netguru.android.inbbbox.feature.user.projects.adapter.shots;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;

public class ProjectShotsViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.project_shot_item_image_view)
    RoundedCornersShotImageView shotImageView;

    public ProjectShotsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_shot_item, parent, false));
    }

    @Override
    public void bind(Shot item) {
        shotImageView.loadShot(item);
    }
}
