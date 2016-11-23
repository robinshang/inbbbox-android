package co.netguru.android.inbbbox.feature.details.recycler;

import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.model.ui.User;

public interface DetailsViewActionCallback {

    void onTeamSelected(Team team);

    void onUserSelected(User user);

    void onShotLikeAction(boolean isLiked);

    void onShotBucket(long shotId, boolean isLikedBucket);


    void onLoadMoreCommentsSelected();
}
