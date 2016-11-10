package co.netguru.android.inbbbox.feature.followers.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.view.RoundedCornersFourImagesView;

public class FollowersThreeShotListViewHolder extends BaseFollowersViewHolder {

    @BindView(R.id.four_image_view)
    RoundedCornersFourImagesView fourImagesView;

    FollowersThreeShotListViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_list_view, parent, false));
    }

    @Override
    public void bind(Follower item) {
        super.bind(item);
        final String url1 = item.shotList().get(0).normalImageUrl();
        loadShotImages(url1,
                item.shotList().get(1).normalImageUrl(),
                item.shotList().get(2).normalImageUrl(), url1);
    }
}
