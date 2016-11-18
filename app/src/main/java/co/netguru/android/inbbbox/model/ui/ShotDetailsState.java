package co.netguru.android.inbbbox.model.ui;

import java.util.List;

public class ShotDetailsState {

    private final boolean isLiked;

    private final boolean isBucketed;

    private final List<Comment> commentList;

    public ShotDetailsState(boolean isLiked, boolean isBucketed, List<Comment> commentList) {
        this.isLiked = isLiked;
        this.isBucketed = isBucketed;
        this.commentList = commentList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isBucketed() {
        return isBucketed;
    }
}
