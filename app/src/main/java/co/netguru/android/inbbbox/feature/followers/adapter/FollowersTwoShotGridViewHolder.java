package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;

public class FollowersTwoShotGridViewHolder extends BaseFollowersViewHolder {

    FollowersTwoShotGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_grid_view, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(Follower item) {
        super.bind(item);
        final String url1 = item.shotList().get(0).normalImageUrl();
        final String url2 = item.shotList().get(1).normalImageUrl();
        loadShotImages(url1, url2, url2, url1);
    }
}

