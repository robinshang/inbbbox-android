package co.netguru.android.inbbbox.feature.followers.details;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowerDetailsViewState extends BaseViewState<FollowerDetailsContract.View, Shot> {

    private Follower follower;

    @Override
    public void apply(FollowerDetailsContract.View view, boolean retained) {
        super.apply(view, retained);
        view.showFollowerData(follower);
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }
}
