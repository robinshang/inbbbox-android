package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.shot.detail.Comment;

public interface DetailsViewActionCallback {

    void onTeamSelected(Team team);

    void onUserSelected(User user);

    void onShotLikeAction(boolean isLiked);

    void onShotBucket(long shotId, boolean isLikedBucket);

    void onLoadMoreCommentsSelected();

    void onCommentDeleteSelected(Comment currentComment);

    void onCommentEditSelected(Comment currentComment);

    void onCommentUpdated(String comment);

    void onCommentDeleteConfirmed();
}
