package co.netguru.android.inbbbox.feature.user;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.user.info.singleuser.UserInfoFragment;
import co.netguru.android.inbbbox.feature.user.info.team.TeamInfoFragment;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsFragment;
import co.netguru.android.inbbbox.feature.user.projects.ProjectsFragment;

public class UserPagerAdapter<T extends Fragment & RefreshableFragment>
        extends FragmentStatePagerAdapter {

    private SparseArray<T> activeRefreshableFragments;
    private User user;

    public UserPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
        activeRefreshableFragments = new SparseArray<>(UserDetailsTabItemType.values().length);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        Fragment result;

        switch (UserDetailsTabItemType.getTabItemForPosition(position)) {
            case SHOTS:
                result = UserShotsFragment.newInstance(user);
                break;
            case INFO:
                result = getInfoFragment();
                break;
            case PROJECTS:
                result = ProjectsFragment.newInstance(user);
                break;
            case BUCKETS:
//                TODO: 20.02 Instantiate BucketsFragment [not in scope of this task]
                result = UserShotsFragment.newInstance(user);
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

    private Fragment getInfoFragment() {
        if (user.type().equals(User.TYPE_SINGLE_USER)) {
            return UserInfoFragment.newInstance(user);
        } else if (user.type().equals(User.TYPE_TEAM)) {
            return TeamInfoFragment.newInstance(user);
        } else {
            throw new IllegalArgumentException(
                    String.format("Wrong user type: %s", user.type()));
        }
    }

}