package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.ShotDetails;

public class ShotDetailsAdapter extends RecyclerView.Adapter<ShotDetailsViewHolder> {

    private static final int STATIC_ITEMS_COUNT = 2;

    private ShotDetails details;

    @Inject
    ShotDetailsAdapter() {
        //di
    }

    @Override
    public ShotDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShotDetailsViewFactory.getViewHolder(viewType, parent);
    }

    @Override
    public void onBindViewHolder(ShotDetailsViewHolder holder, int position) {
        holder.bind(details);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (details != null ? details.comments().size() : 0) + STATIC_ITEMS_COUNT;
    }

    public void setDetails(ShotDetails details) {
        this.details = details;
    }
}
