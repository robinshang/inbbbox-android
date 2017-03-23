package co.netguru.android.inbbbox.app.usercomponent;

import android.content.Context;

import javax.inject.Inject;

import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;

public class UserComponentRestorer {

    private final UserController userController;
    private final Context context;

    @Inject
    public UserComponentRestorer(UserController userController, Context context) {
        this.userController = userController;
        this.context = context;
    }

    public void restoreUserComponent() {
        userController.isGuestModeEnabled()
                .subscribe(guestModeEnabled -> App.initUserComponent(context, guestModeEnabled ?
                        UserModeType.GUEST_USER_MODE : UserModeType.ONLINE_USER_MODE));
    }

}
