package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;

import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.ShotDetails;

abstract class ShotDetailsViewHolder extends BaseViewHolder<ShotDetails> {

    protected final DetailsViewActionCallback actionCallbackListener;
    protected ShotDetails item;

    ShotDetailsViewHolder(View view, DetailsViewActionCallback actionCallbackListener) {
        super(view);
        this.actionCallbackListener = actionCallbackListener;
    }

    @Override
    public void bind(ShotDetails item) {

        this.item = item;
        handleBinding();
    }

    protected abstract void handleBinding();
}
