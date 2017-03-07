package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserShotsAdapter;

public class UserInfoShotsViewHolder extends BaseViewHolder<List<Shot>> {

    @BindView(R.id.user_shots_recyclerView)
    RecyclerView shotsRecyclerView;

    private ShotClickListener shotClickListener;

    UserInfoShotsViewHolder(ViewGroup parent, ShotClickListener shotClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_shots, parent, false));
        this.shotClickListener = shotClickListener;
    }

    @Override
    public void bind(List<Shot> shots) {
        initRecycler(shots);
    }

    private void initRecycler(List<Shot> shots) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                shotsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        UserShotsAdapter userShotsAdapter = new UserShotsAdapter(shotClickListener, null);

        shotsRecyclerView.setLayoutManager(linearLayoutManager);
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.setAdapter(userShotsAdapter);
        shotsRecyclerView.setNestedScrollingEnabled(false);

        // TODO fix that
//        userShotsAdapter.setShots(shots);
    }
}