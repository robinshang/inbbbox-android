package co.netguru.android.inbbbox.feature.shared;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.feature.bucket.BucketsFragment;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;

public class UserDetailsActivityPagerAdapter<T extends Fragment & RefreshableFragment>
        extends FragmentStatePagerAdapter {

    private SparseArray<T> activeRefreshableFragments;

    public UserDetailsActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        activeRefreshableFragments = new SparseArray<>(UserDetailsTabItemType.values().length);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        Fragment result;

        switch (UserDetailsTabItemType.getTabItemForPosition(position)) {
            case SHOTS:
//               TODO: 20.02 Instantiate ShotsFragment [not in scope of this task]
                result = BucketsFragment.newInstance();
                break;
            case INFO:
//                 TODO: 20.02 Instantiate InfoFragment [not in scope of this task]
                result = BucketsFragment.newInstance();
                break;
            case PROJECTS:
//                 TODO: 20.02 Instantiate ProjectsFragment [not in scope of this task]
                result = BucketsFragment.newInstance();
                break;
            case BUCKETS:
//                TODO: 20.02 Instantiate BucketsFragment [not in scope of this task]
                result = BucketsFragment.newInstance();
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "There is no fragment defined for position: %d", position));
        }
        return result;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //noinspection unchecked
        T refreshableFragment = (T) super.instantiateItem(container, position);
        activeRefreshableFragments.put(position, refreshableFragment);
        return refreshableFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        activeRefreshableFragments.delete(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return co.netguru.android.inbbbox.feature.main.adapter.TabItemType.values().length;
    }

    public void refreshFragment(co.netguru.android.inbbbox.feature.main.adapter.TabItemType tabItemType) {
        T refreshableFragment = activeRefreshableFragments.get(tabItemType.getPosition());
        if (refreshableFragment != null) {
            refreshableFragment.refreshFragmentData();
        }
    }
}