package co.netguru.android.inbbbox.feature.follower.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class FollowersTwoShotGridViewHolder extends BaseFollowersViewHolder {

    FollowersTwoShotGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_grid_view, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(User item) {
        super.bind(item);
        final Shot shot1 = item.shotList().get(FIRST_SHOT);
        final Shot shot2 = item.shotList().get(SECOND_SHOT);
        loadShotImages(shot1, shot2, shot2, shot1);
    }
}

