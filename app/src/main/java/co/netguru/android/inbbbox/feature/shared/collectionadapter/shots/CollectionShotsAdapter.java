package co.netguru.android.inbbbox.feature.shared.collectionadapter.shots;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;

public class CollectionShotsAdapter extends RecyclerView.Adapter<CollectionShotsViewHolder> {

    private List<Shot> shotList;
    private ShotClickListener shotClickListener;

    public CollectionShotsAdapter(ShotClickListener shotClickListener) {
        shotList = Collections.emptyList();
        this.shotClickListener = shotClickListener;
    }

    @Override
    public CollectionShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionShotsViewHolder(parent, shotClickListener);
    }

    @Override
    public void onBindViewHolder(CollectionShotsViewHolder holder, int position) {
        holder.bind(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    public void setShotList(List<Shot> shotList) {
        this.shotList = shotList;
        notifyDataSetChanged();
    }
}
