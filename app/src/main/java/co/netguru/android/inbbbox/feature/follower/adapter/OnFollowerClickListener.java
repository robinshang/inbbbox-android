package co.netguru.android.inbbbox.feature.follower.adapter;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

@FunctionalInterface
public interface OnFollowerClickListener {
    void onClick(UserWithShots userWithShots);
}