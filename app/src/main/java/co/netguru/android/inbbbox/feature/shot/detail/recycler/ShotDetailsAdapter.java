package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shot.detail.Comment;
import co.netguru.android.inbbbox.feature.shot.detail.CommentLoadMoreState;

public class ShotDetailsAdapter extends RecyclerView.Adapter<ShotDetailsViewHolder> {

    private static final int LOAD_MORE_ITEM = 1;
    private static final int LOAD_MORE_VIEW_TYPE = -99;
    private static final int USER_INFO_VIEW_TYPE = 0;
    private static final int DESCRIPTION_VIEW_TYPE = 1;
    private static final int STATIC_ITEMS_COUNT = 2;

    private final DetailsViewActionCallback actionCallback;
    private Shot details;
    private List<Comment> comments;
    private CommentLoadMoreState loadMoreState;

    public ShotDetailsAdapter(DetailsViewActionCallback actionCallback) {
        this.actionCallback = actionCallback;
        comments = new ArrayList<>();
        this.loadMoreState = new CommentLoadMoreState();
    }

    @Override
    public ShotDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShotDetailsViewHolder viewHolder;
        switch (viewType) {
            case USER_INFO_VIEW_TYPE:
                viewHolder = new ShotDetailsUserInfoViewHolder(parent, actionCallback);
                break;
            case DESCRIPTION_VIEW_TYPE:
                viewHolder = new ShotDetailsDescriptionViewHolder(parent, actionCallback);
                break;
            case LOAD_MORE_VIEW_TYPE:
                viewHolder = new ShotDetailsLoadMoreViewHolder(parent, actionCallback);
                break;
            default:
                viewHolder = new ShotDetailsCommentViewHolder(parent, actionCallback);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShotDetailsViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case USER_INFO_VIEW_TYPE:
                ((ShotDetailsUserInfoViewHolder) holder).bind(details);
                break;
            case DESCRIPTION_VIEW_TYPE:
                ((ShotDetailsDescriptionViewHolder) holder).bind(details);
                break;
            case LOAD_MORE_VIEW_TYPE:
                ((ShotDetailsLoadMoreViewHolder) holder).bind(loadMoreState);
                break;
            default:
                ((ShotDetailsCommentViewHolder) holder).bind(getComment(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? LOAD_MORE_VIEW_TYPE : position;
    }

    @Override
    public int getItemCount() {
        return (details != null ? comments.size() + LOAD_MORE_ITEM : 0) + STATIC_ITEMS_COUNT;
    }

    public void setDetails(Shot details) {
        this.details = details;
        notifyDataSetChanged();
    }

    public void addComments(List<Comment> comments) {
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    public boolean isInputVisibilityPermitted(int lastVisibleIndex) {
        return lastVisibleIndex == getItemCount() - 1;
    }

    public int addComment(Comment updatedComment) {
        comments.add(0, updatedComment);
        notifyItemInserted(STATIC_ITEMS_COUNT);
        return STATIC_ITEMS_COUNT;
    }

    public void removeComment(Comment commentToRemove) {
        int index = getCommentItemPosition(commentToRemove);
        notifyItemRemoved(index);
        comments.remove(commentToRemove);
    }

    public void updateLoadMoreState(CommentLoadMoreState state) {
        this.loadMoreState = state;
        notifyItemChanged(getItemCount() - 1);
    }

    public void replaceComment(Comment commentToUpdate, Comment updatedComment) {
        int index = comments.indexOf(commentToUpdate);
        comments.remove(index);
        comments.add(index, updatedComment);
        notifyItemChanged(index + STATIC_ITEMS_COUNT);
    }

    private int getCommentItemPosition(Comment commentToRemove) {
        return comments.indexOf(commentToRemove) + STATIC_ITEMS_COUNT;
    }

    private Comment getComment(int position) {
        return comments.get(position - ShotDetailsAdapter.STATIC_ITEMS_COUNT);
    }
}
