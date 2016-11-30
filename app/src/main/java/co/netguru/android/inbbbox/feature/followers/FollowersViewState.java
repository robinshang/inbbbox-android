package co.netguru.android.inbbbox.feature.followers;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Follower;

public class FollowersViewState extends BaseViewState<FollowersContract.View, Follower> {

    @Override
    public void apply(FollowersContract.View view, boolean retained) {
        super.apply(view, retained);
        view.showFollowedUsers(getDataList());
    }
}
