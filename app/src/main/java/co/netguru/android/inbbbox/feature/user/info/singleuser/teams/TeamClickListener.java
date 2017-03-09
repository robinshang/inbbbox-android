package co.netguru.android.inbbbox.feature.user.info.singleuser.teams;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

@FunctionalInterface
public interface TeamClickListener {
    void onTeamClick(User user);
}
