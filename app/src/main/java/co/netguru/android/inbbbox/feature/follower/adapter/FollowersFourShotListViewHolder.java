package co.netguru.android.inbbbox.feature.follower.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

public class FollowersFourShotListViewHolder extends BaseFollowersViewHolder {

    FollowersFourShotListViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(UserWithShots item) {
        super.bind(item);
        loadShotImages(item.shotList().get(FIRST_SHOT),
                item.shotList().get(SECOND_SHOT),
                item.shotList().get(THIRD_SHOT),
                item.shotList().get(FOURTH_SHOT));
    }
}
