package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsViewHolder> {

    private List<Shot> items;
    private ImageLoader imageLoader;

    @Inject
    public ShotsAdapter(ImageLoader imageLoader) {

        this.imageLoader = imageLoader;
    }

    @Override
    public ShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_layout, parent, false);
        return new ShotsViewHolder(itemView, imageLoader);
    }

    @Override
    public void onBindViewHolder(ShotsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setItems(List<Shot> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
