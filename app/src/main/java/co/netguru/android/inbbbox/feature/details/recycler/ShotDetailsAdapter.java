package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

public class ShotDetailsAdapter extends RecyclerView.Adapter<ShotDetailsViewHolder> {

    static final int STATIC_ITEMS_COUNT = 2;

    private final LocalTimeFormatter localTimeFormatter;
    private final DetailsViewActionCallback actionCallback;
    private Shot details;
    private List<Comment> comments;

    @Inject
    public ShotDetailsAdapter(LocalTimeFormatter localTimeFormatter,
                              DetailsViewActionCallback actionCallback) {
        this.localTimeFormatter = localTimeFormatter;
        this.actionCallback = actionCallback;
        comments = Collections.EMPTY_LIST;
    }

    @Override
    public ShotDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShotDetailsViewFactory
                .getViewHolder(viewType, parent, localTimeFormatter, actionCallback);
    }

    @Override
    public void onBindViewHolder(ShotDetailsViewHolder holder, int position) {
        holder.bind(details, comments);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        // TODO: 18.11.2016 handle empty descrption
        return (details != null ? comments.size() : 0) + STATIC_ITEMS_COUNT;
    }

    public void setDetails(Shot details) {
        this.details = details;
        notifyDataSetChanged();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }
}
