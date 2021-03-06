package co.netguru.android.inbbbox.feature.main.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.feature.bucket.currentuser.BucketsFragment;
import co.netguru.android.inbbbox.feature.follower.FollowersFragment;
import co.netguru.android.inbbbox.feature.like.LikesFragment;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingFragment;
import co.netguru.android.inbbbox.feature.shot.ShotsFragment;

public class MainActivityPagerAdapter<T extends Fragment & RefreshableFragment>
        extends FragmentStatePagerAdapter {

    private SparseArray<T> activeRefreshableFragments;
    private boolean isOnboardingPassed;
    private boolean shouldShowShotsAnimation = true;

    public MainActivityPagerAdapter(FragmentManager fm, boolean isOnboardingPassed) {
        super(fm);
        this.isOnboardingPassed = isOnboardingPassed;
        activeRefreshableFragments = new SparseArray<>(TabItemType.values().length);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        switch (TabItemType.getTabItemForPosition(position)) {
            case SHOTS:
                return getProperFragmentForShotsTab();
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
        return TabItemType.values().length;
    }

    public void refreshFragment(TabItemType tabItemType) {
        T refreshableFragment = activeRefreshableFragments.get(tabItemType.getPosition());
        if (refreshableFragment != null) {
            refreshableFragment.refreshFragmentData();
        }
    }

    private Fragment getProperFragmentForShotsTab() {
        Fragment result;
        if (isOnboardingPassed) {
            result = ShotsFragment.newInstance(shouldShowShotsAnimation);
        } else {
            result = OnboardingFragment.newInstance();
        }
        shouldShowShotsAnimation = false;
        return result;
    }
}