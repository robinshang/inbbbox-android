package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;

import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.ShotDetails;

public abstract class ShotDetailsViewHolder extends BaseViewHolder<ShotDetails> {

    protected ShotDetails item;

    ShotDetailsViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(ShotDetails item) {

        this.item = item;
        handleBinding();
    }

    protected abstract void handleBinding();
}
