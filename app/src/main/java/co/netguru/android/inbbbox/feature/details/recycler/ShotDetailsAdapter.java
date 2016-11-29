package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotDetailsAdapter extends RecyclerView.Adapter<ShotDetailsViewHolder> {

    static final int STATIC_ITEMS_COUNT = 2;
    private static final int LOAD_MORE_ITEM = 1;

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
        return ShotDetailsViewFactory
                .getViewHolder(viewType, parent, actionCallback);
    }

    @Override
    public void onBindViewHolder(ShotDetailsViewHolder holder, int position) {
        holder.bind(details, comments);
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? ShotDetailsViewFactory.LOAD_MORE_ITEM_TYPE : position;
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
