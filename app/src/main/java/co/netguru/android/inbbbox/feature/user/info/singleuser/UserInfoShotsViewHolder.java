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
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.shotsadapter.SharedShotsAdapter;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserShotsAdapter;

public class UserInfoShotsViewHolder extends BaseViewHolder<List<Shot>> {

    @BindView(R.id.user_shots_recyclerView)
    RecyclerView shotsRecyclerView;

    private ShotClickListener shotClickListener;

    private ShotPeekAndPop shotPeekAndPop;

    UserInfoShotsViewHolder(ViewGroup parent, ShotClickListener shotClickListener,
                            ShotPeekAndPop shotPeekAndPop) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_shots, parent, false));
        this.shotClickListener = shotClickListener;
        this.shotPeekAndPop = shotPeekAndPop;
    }

    @Override
    public void bind(List<Shot> shots) {
        initRecycler(shots);
    }

    private void initRecycler(List<Shot> shots) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                shotsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        SharedShotsAdapter shotsAdapter = new SharedShotsAdapter(shotClickListener, shotPeekAndPop);
        shotsAdapter.setShots(shots);
        shotsRecyclerView.setLayoutManager(linearLayoutManager);
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.setAdapter(shotsAdapter);
        shotsRecyclerView.setNestedScrollingEnabled(false);
    }
}