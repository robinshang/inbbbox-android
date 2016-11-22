package co.netguru.android.inbbbox.feature.details.recycler;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;

abstract class ShotDetailsViewHolder extends RecyclerView.ViewHolder {

    protected final DetailsViewActionCallback actionCallbackListener;
    protected Shot item;
    protected List<Comment> commentList;

    ShotDetailsViewHolder(View view, DetailsViewActionCallback actionCallbackListener) {
        super(view);
        this.actionCallbackListener = actionCallbackListener;
        ButterKnife.bind(this, view);
    }

    public void bind(Shot item, List<Comment> comments) {

        this.item = item;
        this.commentList = comments;
        handleBinding();
    }

    protected abstract void handleBinding();
}
