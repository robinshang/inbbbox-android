package co.netguru.android.inbbbox.feature.like.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;

import static co.netguru.android.inbbbox.Constants.UNDEFINED;

@FragmentScope
public class LikesAdapter extends RecyclerView.Adapter<LikesViewHolder> {

    private final ShotClickListener likeClickListener;
    private final ShotPeekAndPop peekAndPop;

    @NonNull
    private List<Shot> likeList;

    @Inject
    public LikesAdapter(ShotClickListener likeClickListener, ShotPeekAndPop peekAndPop) {
        this.likeClickListener = likeClickListener;
        this.peekAndPop = peekAndPop;
        likeList = Collections.emptyList();
    }

    @Override
    public LikesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LikesViewHolder(parent, likeClickListener);
    }

    @Override
    public void onBindViewHolder(LikesViewHolder holder, int position) {
        peekAndPop.addLongClickView(holder.imageView, position);
        holder.bind(likeList.get(position));
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public List<Shot> getData() {
        return likeList;
    }

    public void setLikeList(List<Shot> likeList) {
        this.likeList = likeList;
        notifyDataSetChanged();
    }

    public void addMoreLikes(List<Shot> likeList) {
        final int currentSize = this.likeList.size();
        this.likeList.addAll(likeList);
        notifyItemRangeChanged(currentSize - 1, likeList.size());
    }

    public void addLikeOnTop(Shot likedShot) {
        final int shotPosition = getShotIndexIfExists(likedShot);
        if (shotPosition != UNDEFINED) {
            this.likeList.set(shotPosition, likedShot);
            notifyItemChanged(shotPosition);
        } else {
            this.likeList.add(0, likedShot);
            notifyItemRangeChanged(0, 1);
        }
    }

    public void removeLike(Shot unlikedShot) {
        final int shotPosition = getShotIndexIfExists(unlikedShot);
        if (shotPosition != UNDEFINED) {
            likeList.remove(shotPosition);
            notifyItemRemoved(shotPosition);
        }
    }

    private int getShotIndexIfExists(Shot likedShot) {
        for (int i = 0; i < likeList.size(); i++) {
            final Shot currentShot = likeList.get(i);
            if (currentShot.id() == likedShot.id()) {
                return i;
            }
        }
        return UNDEFINED;
    }
}
