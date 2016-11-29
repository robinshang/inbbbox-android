package co.netguru.android.inbbbox.feature.details.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;

abstract class ShotDetailsViewHolder extends RecyclerView.ViewHolder {

    protected final DetailsViewActionCallback actionCallbackListener;

    ShotDetailsViewHolder(View view, DetailsViewActionCallback actionCallbackListener) {
        super(view);
        this.actionCallbackListener = actionCallbackListener;
        ButterKnife.bind(this, view);
    }

    public abstract void  bind(@Nullable Shot item, @Nullable Comment comments);

}
