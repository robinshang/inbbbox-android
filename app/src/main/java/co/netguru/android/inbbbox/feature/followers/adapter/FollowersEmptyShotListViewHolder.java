package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;

public class FollowersEmptyShotListViewHolder extends BaseFollowersViewHolder {

    FollowersEmptyShotListViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_list_view, parent, false));
    }
}
