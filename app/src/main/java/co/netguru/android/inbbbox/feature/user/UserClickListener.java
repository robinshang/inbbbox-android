package co.netguru.android.inbbbox.feature.user;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

@FunctionalInterface
public interface UserClickListener {
    void onUserClick(User user);
}
