package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowersThreeShotGridViewHolder extends BaseFollowersViewHolder {

    FollowersThreeShotGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_grid_view, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(Follower item) {
        super.bind(item);
        final Shot shot = item.shotList().get(FIRST_SHOT);
        loadShotImages(shot,
                item.shotList().get(SECOND_SHOT),
                item.shotList().get(THIRD_SHOT), shot);
    }
}

