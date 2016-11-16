package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;

public class FollowersFourShotListViewHolder extends BaseFollowersViewHolder {

    FollowersFourShotListViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_list_view, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(Follower item) {
        super.bind(item);
        loadShotImages(item.shotList().get(0).normalImageUrl(),
                item.shotList().get(1).normalImageUrl(),
                item.shotList().get(2).normalImageUrl(),
                item.shotList().get(3).normalImageUrl());
    }
}
