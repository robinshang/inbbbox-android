package co.netguru.android.inbbbox.feature.user;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.bucket.BucketsFragment;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsFragment;

public class UserPagerAdapter<T extends Fragment & RefreshableFragment>
        extends FragmentStatePagerAdapter {

    private SparseArray<T> activeRefreshableFragments;
    private UserWithShots userWithShots;

    public UserPagerAdapter(FragmentManager fm, UserWithShots userWithShots) {
        super(fm);
        this.userWithShots = userWithShots;
        activeRefreshableFragments = new SparseArray<>(UserDetailsTabItemType.values().length);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        Fragment result;

        switch (UserDetailsTabItemType.getTabItemForPosition(position)) {
            case SHOTS:
                result = UserShotsFragment.newInstance(userWithShots);
                break;
            case INFO:
//                 TODO: 20.02 Instantiate InfoFragment [not in scope of this task]
                result = UserShotsFragment.newInstance(userWithShots);
                break;
            case PROJECTS:
//                 TODO: 20.02 Instantiate ProjectsFragment [not in scope of this task]
                result = UserShotsFragment.newInstance(userWithShots);
                break;
            case BUCKETS:
//                TODO: 20.02 Instantiate BucketsFragment [not in scope of this task]
                result = UserShotsFragment.newInstance(userWithShots);
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
        return UserDetailsTabItemType.values().length;
    }
}