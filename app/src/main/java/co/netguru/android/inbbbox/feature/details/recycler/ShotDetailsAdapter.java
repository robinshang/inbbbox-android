package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.ViewGroup;

import java.util.Collections;

import javax.inject.Inject;

import co.netguru.android.inbbbox.feature.common.BaseRecyclerAdapter;
import co.netguru.android.inbbbox.model.ui.ShotDetails;

public class ShotDetailsAdapter extends BaseRecyclerAdapter<ShotDetails, ShotDetailsCommentViewHolder> {

    @Inject
    ShotDetailsAdapter() {
        items = Collections.emptyList();
    }

    @Override
    public ShotDetailsCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ShotDetailsCommentViewHolder holder, int position) {

    }
}
