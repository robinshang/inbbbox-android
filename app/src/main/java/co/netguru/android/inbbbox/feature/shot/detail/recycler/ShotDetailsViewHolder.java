package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

abstract class ShotDetailsViewHolder<T> extends RecyclerView.ViewHolder {

    protected final DetailsViewActionCallback actionCallbackListener;

    ShotDetailsViewHolder(View view, DetailsViewActionCallback actionCallbackListener) {
        super(view);
        this.actionCallbackListener = actionCallbackListener;
        ButterKnife.bind(this, view);
    }

    public abstract void bind(@NonNull T object);

}
