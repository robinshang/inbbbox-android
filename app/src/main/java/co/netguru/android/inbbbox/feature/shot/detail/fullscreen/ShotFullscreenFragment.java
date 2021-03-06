package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

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
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shared.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;

public class ShotFullscreenFragment extends
        BaseMvpFragment<ShotFullscreenContract.View, ShotFullscreenContract.Presenter> implements
        ShotFullscreenContract.View {

    public static final String TAG = ShotFullscreenFragment.class.getSimpleName();
    public static final String KEY_PREVIEW_SHOT_INDEX = "key:preview_shot_index";
    public static final String KEY_ALL_SHOTS = "key:all_shots";
    public static final String KEY_DETAILS_REQUEST = "key:details_request";
    private static final int SHOTS_TO_LOAD_MORE = 5;

    @BindView(R.id.shot_fullscreen_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @Inject
    ShotFullscreenAdapter adapter;

    @Inject
    AnalyticsEventLogger analyticsEventLogger;

    private ShotFullscreenComponent component;

    private int currentShotIndex;

    public static ShotFullscreenFragment newInstance(List<Shot> allShots, int previewShotIndex,
                                                     ShotDetailsRequest detailsRequest) {
        Bundle args = new Bundle();
        args.putInt(KEY_PREVIEW_SHOT_INDEX, previewShotIndex);
        args.putParcelable(KEY_DETAILS_REQUEST, detailsRequest);

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
        analyticsEventLogger.logEventScreenShotFullscreen();
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
        initRecyclerView();
    }

    @OnClick(R.id.shot_fullscreen_back)
    void onBackPressed() {
        getPresenter().onBackArrowPressed();
    }

    @Override
    public void previewShots(List<Shot> allShots, int previewShotIndex) {
        this.currentShotIndex = previewShotIndex;
        adapter.setItems(allShots);
    }

    @Override
    public void showMoreItems(List<Shot> shots) {
        adapter.addMoreItems(shots);
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    private void initRecyclerView() {
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.HORIZONTAL, false));
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().onRequestMoreData();
                analyticsEventLogger.logEventFullscreenSwipe(SHOTS_TO_LOAD_MORE);
            }
        });
        shotsRecyclerView.post(() -> shotsRecyclerView.scrollToPosition(currentShotIndex));
    }

    private void initComponent() {
        int previewShotIndex = getArguments().getInt(KEY_PREVIEW_SHOT_INDEX);
        List<Shot> shots = getArguments().getParcelableArrayList(KEY_ALL_SHOTS);
        ShotDetailsRequest detailsRequest = getArguments().getParcelable(KEY_DETAILS_REQUEST);

        component = App.getUserComponent(getContext())
                .plus(new ShotFullscreenModule(shots, previewShotIndex, detailsRequest));
        component.inject(this);
    }
}