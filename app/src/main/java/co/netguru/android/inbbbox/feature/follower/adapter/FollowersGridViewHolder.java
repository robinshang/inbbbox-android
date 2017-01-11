package co.netguru.android.inbbbox.feature.follower.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class FollowersGridViewHolder extends BaseFollowersViewHolder {

    public FollowersGridViewHolder(ViewGroup parent, OnFollowerClickListener onFollowerClickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_grid_item, parent, false), onFollowerClickListener);
    }

    @Override
    public void bind(UserWithShots item) {
        super.bind(item);

        if(item.shotList().size() > 0) {
            Shot[] shots = new Shot[4];
            for (int i = 0; i < 4; i++) {
                shots[i] = item.shotList().get(i % item.shotList().size());
            }
            loadShotImages(shots[0], shots[1], shots[2], shots[3]);
        }
    }
}

