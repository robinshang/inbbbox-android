package co.netguru.android.inbbbox.feature.main.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.enumeration.TabItemType;
import co.netguru.android.inbbbox.feature.buckets.BucketsFragment;
import co.netguru.android.inbbbox.feature.followers.FollowersFragment;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import co.netguru.android.inbbbox.feature.shots.ShotsFragment;

public class MainActivityPagerAdapter<T extends Fragment & RefreshableFragment> extends FragmentStatePagerAdapter {

    private SparseArray<T> fragments;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new SparseArray<>(TabItemType.values().length);
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
    public Object instantiateItem(ViewGroup container, int position) {
        //noinspection unchecked
        T fragment = (T) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.delete(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return TabItemType.values().length;
    }

    public void refreshFragment(TabItemType tabItemType) {
        T fragment = fragments.get(tabItemType.getPosition());
        if (fragment != null) {
            fragment.refreshFragmentData();
        }
    }
}