package co.netguru.android.inbbbox.feature.main.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.netguru.android.inbbbox.feature.buckets.BucketsFragment;
import co.netguru.android.inbbbox.feature.followers.FollowersFragment;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import co.netguru.android.inbbbox.model.ui.TabItemType;
import co.netguru.android.inbbbox.feature.shots.ShotsFragment;

public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        switch (TabItemType.getTabItemForPosition(position)) {
            case SHOTS:
                return ShotsFragment.newInstance();
            case LIKES:
                return LikesFragment.newInstance();
            case BUCKETS:
                return BucketsFragment.newInstance();
            case FOLLOWERS:
                return FollowersFragment.newInstance();
            default:
                throw new IllegalArgumentException(String.format(
                        "There is no fragment defined for position: %d", position));
        }
    }

    @Override
    public int getCount() {
        return TabItemType.values().length;
    }
}