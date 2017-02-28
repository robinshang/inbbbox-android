package co.netguru.android.inbbbox.feature.user;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

@FunctionalInterface
public interface UserClickListener {
    void onUserClick(User user);
}
