package co.netguru.android.inbbbox.feature.follower.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class FollowersOneShotGridViewHolder extends BaseFollowersViewHolder {

    FollowersOneShotGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_grid_view, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(User item) {
        super.bind(item);
        final Shot shot = item.shotList().get(FIRST_SHOT);
        loadShotImages(shot, shot, shot, shot);
    }
}

