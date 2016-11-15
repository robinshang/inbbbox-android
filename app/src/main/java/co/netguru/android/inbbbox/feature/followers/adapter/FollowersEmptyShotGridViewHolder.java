package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;

public class FollowersEmptyShotGridViewHolder extends BaseFollowersViewHolder {

    FollowersEmptyShotGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_grid_view, parent, false), onFollowerClickListener);
    }
}
