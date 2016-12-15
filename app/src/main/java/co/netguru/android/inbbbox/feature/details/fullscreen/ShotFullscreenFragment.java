package co.netguru.android.inbbbox.feature.details.fullscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotFullscreenComponent;
import co.netguru.android.inbbbox.di.module.ShotFullscreenModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class ShotFullscreenFragment extends
        BaseMvpFragment<ShotFullscreenContract.View, ShotFullscreenContract.Presenter> implements
        ShotFullscreenContract.View {

    public static final String TAG = ShotFullscreenFragment.class.getSimpleName();
    public static final String KEY_SHOT = "key:shot";
    public static final String KEY_ALL_SHOTS = "key:all_shots";
    private static final int SHOTS_TO_LOAD_MORE = 5;

    @BindView(R.id.shot_fullscreen_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @Inject
    ShotFullscreenAdapter adapter;

    private ShotFullscreenComponent component;

    public static ShotFullscreenFragment newInstance(Shot shot, List<Shot> allShots) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_SHOT, shot);

        if (allShots instanceof ArrayList) {
            args.putParcelableArrayList(KEY_ALL_SHOTS, (ArrayList<Shot>) allShots);
        } else {
            args.putParcelableArrayList(KEY_ALL_SHOTS, new ArrayList<Shot>(allShots));
        }

        ShotFullscreenFragment shotFullscreenFragment = new ShotFullscreenFragment();
        shotFullscreenFragment.setArguments(args);

        return shotFullscreenFragment;
    }

    @Override
    public ShotFullscreenContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shot_fullscreen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Shot shot = getArguments().getParcelable(KEY_SHOT);
        List<Shot> allShots = getArguments().getParcelableArrayList(KEY_ALL_SHOTS);

        getPresenter().onViewCreated(shot, allShots);
    }

    @Override
    public void previewShot(Shot shot, List<Shot> allShots) {
        adapter.setItems(allShots);
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.HORIZONTAL, false));
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().onRequestMoreData();
            }
        });
        shotsRecyclerView.scrollToPosition(allShots.indexOf(shot));
    }

    @Override
    public void showMoreItems(List<Shot> shots) {
        adapter.addMoreItems(shots);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plusShotFullscreenComponent(new ShotFullscreenModule());
        component.inject(this);
    }
}