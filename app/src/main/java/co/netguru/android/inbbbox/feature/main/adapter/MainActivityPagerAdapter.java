package co.netguru.android.inbbbox.feature.main.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.netguru.android.inbbbox.feature.main.MenuItem;

public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        switch (MenuItem.getMenuItemForPosition(position)) {
            case SHOTS:
                return new Fragment();
            case LIKES:
                return new Fragment();
            case BUCKETS:
                return new Fragment();
            case FOLLOWERS:
                return new Fragment();
            default:
                throw new IllegalArgumentException(String.format(
                        "There is no fragment defined for position: %d", position));
        }
    }

    @Override
    public int getCount() {
        return MenuItem.values().length;
    }
}