package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowerDetailsViewState extends RetainingLceViewState<List<Shot>, FollowerDetailsContract.View> {

    private Follower follower;

    @Override
    public void apply(FollowerDetailsContract.View view, boolean retained) {
        super.apply(view, retained);
        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showFollowerData(follower);
        }
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }
}
