package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotDetailsAdapter extends RecyclerView.Adapter<ShotDetailsViewHolder> {

    private static final int LOAD_MORE_ITEM = 1;
    private static final int LOAD_MORE_VIEW_TYPE = -99;
    private static final int USER_INFO_VIEW_TYPE = 0;
    private static final int DESCRIPTION_VIEW_TYPE = 1;
    private static final int STATIC_ITEMS_COUNT = 2;

    private final DetailsViewActionCallback actionCallback;
    private Shot details;
    private List<Comment> comments;

    @Inject
    public ShotDetailsAdapter(DetailsViewActionCallback actionCallback) {
        this.actionCallback = actionCallback;
        comments = Collections.emptyList();
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
        Comment comment = null;
        if (!comments.isEmpty() && position > STATIC_ITEMS_COUNT-1 && position < getItemCount()-1) {
            comment = comments.get(position - ShotDetailsAdapter.STATIC_ITEMS_COUNT);
        }
        holder.bind(details, comment);
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public boolean isInputVisibilityPermitted(int lastVisibleIndex) {
        return lastVisibleIndex == getItemCount() - 1;
    }
}
