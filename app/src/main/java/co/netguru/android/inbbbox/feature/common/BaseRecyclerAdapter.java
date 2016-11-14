package co.netguru.android.inbbbox.feature.common;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerAdapter<ItemsType, HolderType extends BaseViewHolder> extends RecyclerView.Adapter<HolderType> {

    protected List<ItemsType> items;

    public void setItems(List<ItemsType> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
