package co.netguru.android.inbbbox.feature.shared.collectionadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class CollectionAdapter<C extends ShotsCollection> extends RecyclerView.Adapter<CollectionViewHolder> {

    private final OnGetMoreCollectionShotsListener onGetMoreCollectionShotsListener;
    private final CollectionClickListener collectionClickListener;

    private List<C> collectionsList;

    public CollectionAdapter(@NonNull OnGetMoreCollectionShotsListener onGetMoreCollectionShotsListener,
                             @NonNull CollectionClickListener collectionClickListener) {
        this.onGetMoreCollectionShotsListener = onGetMoreCollectionShotsListener;
        this.collectionClickListener = collectionClickListener;
        collectionsList = Collections.emptyList();
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionViewHolder(parent, onGetMoreCollectionShotsListener, collectionClickListener);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        holder.bind(collectionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return collectionsList.size();
    }

    public List<C> getData() {
        return collectionsList;
    }

    public void addMoreCollections(List<C> collections) {
        final int currentSize = this.collectionsList.size();
        this.collectionsList.addAll(collections);
        notifyItemRangeChanged(currentSize - 1, collections.size());
    }

    public List<C> getCollectionsList() {
        return collectionsList;
    }

    public void setCollectionsList(List<C> collectionsList) {
        this.collectionsList = collectionsList;
        notifyDataSetChanged();
    }

    public void addMoreCollectionShots(long collectionId, List<Shot> shotList, int shotsPerPage) {
        final int index = findCollectionIndex(collectionId);
        final C collection = collectionsList.get(index);
        collection.shots().addAll(shotList);
        C newCollection = (C) collection.updatePageStatus(shotList.size() >= shotsPerPage);
        updateCollection(index, newCollection);
    }

    public int findCollectionIndex(long projectId) {
        for (int i = 0; i < collectionsList.size(); i++) {
            if (collectionsList.get(i).getId() == projectId) {
                return i;
            }
        }
        throw new IllegalArgumentException("There is no collection with id: " + projectId);
    }

    public void updateCollection(int index, C collection) {
        collectionsList.set(index, collection);
        notifyItemChanged(index);
    }

    @FunctionalInterface
    public interface OnGetMoreCollectionShotsListener<C extends ShotsCollection> {
        void onGetMoreCollectionShots(C collection);
    }
}
