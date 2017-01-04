package co.netguru.android.inbbbox.feature.follower.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class FollowersThreeShotListViewHolder extends BaseFollowersViewHolder {

    FollowersThreeShotListViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(UserWithShots item) {
        super.bind(item);
        final Shot shot = item.shotList().get(FIRST_SHOT);
        loadShotImages(shot,
                item.shotList().get(SECOND_SHOT),
                item.shotList().get(THIRD_SHOT), shot);
    }
}
