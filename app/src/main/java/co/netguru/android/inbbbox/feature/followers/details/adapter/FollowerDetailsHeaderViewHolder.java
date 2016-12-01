package co.netguru.android.inbbbox.feature.followers.details.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Follower;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowerDetailsHeaderViewHolder extends BaseViewHolder<Follower> {

    @BindView(R.id.follower_detail_user_photo)
    CircleImageView userPhoto;

    FollowerDetailsHeaderViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_detail_item_header_view, parent, false));
    }

    @Override
    public void bind(Follower item) {
        Glide.with(itemView.getContext())
                .load(item.avatarUrl())
                .animate(android.R.anim.fade_in)
                .into(userPhoto);
    }
}
